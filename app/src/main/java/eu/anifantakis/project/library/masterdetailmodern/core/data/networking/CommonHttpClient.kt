package eu.anifantakis.project.library.masterdetailmodern.core.data.networking

import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataError
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataResult
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.nio.channels.UnresolvedAddressException
import kotlin.coroutines.cancellation.CancellationException

abstract class CommonHttpClient(
    private val baseUrl: String,
    private val logging: Boolean = true
) {
    private val logTag: String by lazy {
        this::class.simpleName ?: "Unknown"
    }

    protected abstract val additionalConfig: (HttpClientConfig<CIOEngineConfig>.() -> Unit)?

    @PublishedApi
    internal val client: HttpClient by lazy {
        HttpClient(CIO) {
            // Install standard plugins
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }

            if (logging) {
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            Timber.tag("$logTag Klient").d(message)
                        }
                    }
                    level = LogLevel.ALL
                }
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
                url {
                    takeFrom(baseUrl)
                }
            }

            // Apply additional configuration if provided
            additionalConfig?.invoke(this)
        }
    }

    suspend inline fun <reified T> responseToResult(response: HttpResponse): DataResult<T, DataError.Network> {
        return when (response.status.value) {
            in 200..299 -> DataResult.Success(response.body())
            401 -> DataResult.Failure(DataError.Network.UNAUTHORIZED)
            408 -> DataResult.Failure(DataError.Network.REQUEST_TIMEOUT)
            409 -> DataResult.Failure(DataError.Network.CONFLICT)
            413 -> DataResult.Failure(DataError.Network.PAYLOAD_TOO_LARGE)
            429 -> DataResult.Failure(DataError.Network.TOO_MANY_REQUESTS)
            in 500..599 -> DataResult.Failure(DataError.Network.SERVER_ERROR)
            else -> DataResult.Failure(DataError.Network.UNKNOWN)
        }
    }

    // Error handling methods
    suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): DataResult<T, DataError.Network> {
        return try {
            responseToResult(execute())
        } catch (e: UnresolvedAddressException) {
            e.printStackTrace()
            DataResult.Failure(DataError.Network.NO_INTERNET)
        } catch (e: SerializationException) {
            e.printStackTrace()
            DataResult.Failure(DataError.Network.SERIALIZATION)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            DataResult.Failure(DataError.Network.UNKNOWN)
        }
    }

    // Networking methods
    suspend inline fun <reified Response : Any> get(
        route: String,
        queryParameters: Map<String, Any?> = mapOf()
    ): DataResult<Response, DataError.Network> {
        return safeCall {
            client.get {
                url(route)
                queryParameters.forEach { (key, value) ->
                    parameter(key, value)
                }
            }
        }
    }

    suspend inline fun <reified Request : Any, reified Response : Any> post(
        route: String,
        body: Request
    ): DataResult<Response, DataError.Network> {
        return safeCall {
            client.post {
                url(route)
                setBody(body)
            }
        }
    }

    suspend inline fun <reified Response : Any> HttpClient.delete(
        route: String,
        queryParameters: Map<String, Any?> = mapOf()
    ): DataResult<Response, DataError.Network> {
        return safeCall {
            client.delete {
                url(route)
                queryParameters.forEach { (key, value) ->
                    parameter(key, value)
                }
            }
        }
    }
}

package eu.anifantakis.project.library.masterdetailmodern.core.data.networking

import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataError
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.Result
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
import io.ktor.client.request.header
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
    open val tag: String,
    open val baseUrl: String,
    open val apiKey: String? = null,
    private val additionalConfig: (HttpClientConfig<CIOEngineConfig>.() -> Unit)? = null
) {
    val client: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Timber.tag("$tag Http Client").d(message)
                }
            }
            level = LogLevel.ALL
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
            if (apiKey != null) {
                header("x-api-key", apiKey)
            }
            url {
                takeFrom(baseUrl)
            }
        }

        // Apply additional configuration if provided
        additionalConfig?.invoke(this)
    }

    // Networking methods
    suspend inline fun <reified Response : Any> get(
        route: String,
        queryParameters: Map<String, Any?> = mapOf()
    ): Result<Response, DataError.Network> {
        return client.get(route, queryParameters)
    }

    suspend inline fun <reified Request : Any, reified Response : Any> post(
        route: String,
        body: Request
    ): Result<Response, DataError.Network> {
        return client.post(route, body)
    }

    suspend inline fun <reified Response : Any> delete(
        route: String,
        queryParameters: Map<String, Any?> = mapOf()
    ): Result<Response, DataError.Network> {
        return client.delete(route, queryParameters)
    }
}

// Networking methods
suspend inline fun <reified Response : Any> HttpClient.get(
    route: String,
    queryParameters: Map<String, Any?> = mapOf()
): Result<Response, DataError.Network> {
    return safeCall {
        this.get {
            url(route)
            queryParameters.forEach { (key, value) ->
                parameter(key, value)
            }
        }
    }
}

suspend inline fun <reified Request : Any, reified Response : Any> HttpClient.post(
    route: String,
    body: Request
): Result<Response, DataError.Network> {
    return safeCall {
        this.post {
            url(route)
            setBody(body)
        }
    }
}

suspend inline fun <reified Response : Any> HttpClient.delete(
    route: String,
    queryParameters: Map<String, Any?> = mapOf()
): Result<Response, DataError.Network> {
    return safeCall {
        this.delete {
            url(route)
            queryParameters.forEach { (key, value) ->
                parameter(key, value)
            }
        }
    }
}

// Error handling methods
suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): Result<T, DataError.Network> {
    return try {
        responseToResult(execute())
    } catch (e: UnresolvedAddressException) {
        e.printStackTrace()
        Result.Failure(DataError.Network.NO_INTERNET)
    } catch (e: SerializationException) {
        e.printStackTrace()
        Result.Failure(DataError.Network.SERIALIZATION)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        e.printStackTrace()
        Result.Failure(DataError.Network.UNKNOWN)
    }
}

suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, DataError.Network> {
    return when (response.status.value) {
        in 200..299 -> Result.Success(response.body())
        401 -> Result.Failure(DataError.Network.UNAUTHORIZED)
        408 -> Result.Failure(DataError.Network.REQUEST_TIMEOUT)
        409 -> Result.Failure(DataError.Network.CONFLICT)
        413 -> Result.Failure(DataError.Network.PAYLOAD_TOO_LARGE)
        429 -> Result.Failure(DataError.Network.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Failure(DataError.Network.SERVER_ERROR)
        else -> Result.Failure(DataError.Network.UNKNOWN)
    }
}
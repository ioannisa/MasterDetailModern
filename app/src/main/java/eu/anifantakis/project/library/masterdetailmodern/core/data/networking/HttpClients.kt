package eu.anifantakis.project.library.masterdetailmodern.core.data.networking

import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataError
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber

abstract class CommonHttpClient(open val tag: String, open val baseUrl: String, open val apiKey: String? = null) {

    var client: HttpClient

    init {
        client = HttpClient(CIO) {
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
        }
    }

    suspend inline fun <reified Response : Any> get(
        route: String,
        queryParameters: Map<String, Any?> = mapOf()
    ): Result<Response, DataError.Network> {
        return client.get(route, queryParameters)
    }

    suspend inline fun <reified Request, reified Response: Any> post(
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

class AuthHttpClient(override val tag: String, override val baseUrl: String, override val apiKey: String? = null) : CommonHttpClient(tag, baseUrl, apiKey)
class MoviesHttpClient(override val tag: String, override val baseUrl: String, override val apiKey: String? = null) : CommonHttpClient(tag, baseUrl, apiKey)
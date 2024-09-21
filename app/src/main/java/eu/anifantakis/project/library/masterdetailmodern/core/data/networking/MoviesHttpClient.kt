package eu.anifantakis.project.library.masterdetailmodern.core.data.networking

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.takeFrom

class MoviesHttpClient(
    tag: String,
    baseUrl: String,
    private val apiKey: String? = null
) : CommonHttpClient(tag, baseUrl) {

    override val additionalConfig: (HttpClientConfig<CIOEngineConfig>.() -> Unit)? = {
        defaultRequest {
            contentType(ContentType.Application.Json)
            url {
                takeFrom(baseUrl)
                if (apiKey != null) {
                    parameters.append("api_key", apiKey)
                }
            }
        }

        // Or if you prefer headers
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
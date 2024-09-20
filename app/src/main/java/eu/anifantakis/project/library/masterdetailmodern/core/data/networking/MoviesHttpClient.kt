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
    apiKey: String? = null,
) : CommonHttpClient(
    tag = tag,
    baseUrl = baseUrl,

    additionalConfig = {

        // additional configuration with api key as param
        defaultRequest {
            contentType(ContentType.Application.Json)
            url {
                takeFrom(baseUrl)
                if (apiKey != null) {
                    parameters.append("api_key", apiKey)
                }
            }
        }


        // additional configuration with api key as header param
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

)
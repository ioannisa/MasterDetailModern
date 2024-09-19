package eu.anifantakis.project.library.masterdetailmodern.core.data.networking

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIOEngineConfig

class MoviesHttpClient(
    tag: String,
    baseUrl: String,
    apiKey: String? = null,
    moviesConfig: (HttpClientConfig<CIOEngineConfig>.() -> Unit)? = null
) : CommonHttpClient(
    tag = tag,
    baseUrl = baseUrl,
    apiKey = apiKey,
    additionalConfig = moviesConfig
)
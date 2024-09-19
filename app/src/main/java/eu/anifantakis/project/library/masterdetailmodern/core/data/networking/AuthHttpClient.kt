package eu.anifantakis.project.library.masterdetailmodern.core.data.networking

import eu.anifantakis.lib.securepersist.PersistManager
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.Result
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import kotlinx.serialization.Serializable

class AuthHttpClient(
    tag: String,
    baseUrl: String,
    apiKey: String? = null,
    persistManager: PersistManager,
    authConfig: HttpClientConfig<CIOEngineConfig>.() -> Unit = {

        // https://dummyjson.com/docs/auth
        install(Auth) {
            bearer {
                var accessToken by persistManager.preference("")
                var refreshToken by persistManager.preference("")

                // Default bearer token configuration
                loadTokens {
                    BearerTokens(
                        accessToken = accessToken,
                        refreshToken = refreshToken
                    )
                }

                refreshTokens {
                    val response = client.post<AccessTokenRequest, AccessTokenResponse>(
                        route = "/auth/refresh",
                        body = AccessTokenRequest(
                            refreshToken = "",
                            expiresInMins = 1
                        )
                    )

                    if (response is Result.Success) {
                        accessToken = response.data.token
                        refreshToken = response.data.refreshToken
                    } else {
                        accessToken = ""
                        refreshToken = ""
                    }

                    BearerTokens(
                        accessToken = accessToken,
                        refreshToken = refreshToken
                    )
                }
            }
        }
    }
) : CommonHttpClient(tag, baseUrl, apiKey, authConfig) {

    @Serializable
    private data class AccessTokenRequest(
        val refreshToken: String,
        val expiresInMins: Int
    )

    @Serializable
    private data class AccessTokenResponse(
        val token: String,
        val refreshToken: String
    )
}
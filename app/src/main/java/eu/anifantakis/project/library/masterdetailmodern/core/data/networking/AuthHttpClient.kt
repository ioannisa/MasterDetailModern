package eu.anifantakis.project.library.masterdetailmodern.core.data.networking

import eu.anifantakis.lib.securepersist.PersistManager
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataResult
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import kotlinx.serialization.Serializable

class AuthHttpClient(
    tag: String,
    baseUrl: String,
    persistManager: PersistManager
) : CommonHttpClient(tag, baseUrl) {

    // https://github.com/ioannisa/SecurePersist
    private var authInfo by persistManager.preference(AuthInfo())

    override val additionalConfig: (HttpClientConfig<CIOEngineConfig>.() -> Unit) = {
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(
                        accessToken = authInfo.accessToken,
                        refreshToken = authInfo.refreshToken
                    )
                }

                refreshTokens {
                    val response = post<AccessTokenRequest, AccessTokenResponse>(
                        route = "/auth/refresh",
                        body = AccessTokenRequest(
                            refreshToken = authInfo.refreshToken,
                            expiresInMins = 1
                        )
                    )

                    when (response) {
                        is DataResult.Success -> {
                            persistAuthInfo(
                                accessToken = response.data.token,
                                refreshToken = response.data.refreshToken
                            )
                        }
                        is DataResult.Failure -> {
                            persistAuthInfo("", "", 0)
                        }
                    }

                    BearerTokens(
                        accessToken = authInfo.accessToken,
                        refreshToken = authInfo.refreshToken
                    )
                }
            }
        }
    }

    fun persistAuthInfo(accessToken: String, refreshToken: String, userId: Int? = null) {

        // as simple as that, just change the value of your object and it gets encrypted and saved
        this.authInfo = authInfo.copy(
            accessToken = accessToken,
            refreshToken = refreshToken,
            userId = userId ?: this.authInfo.userId
        )
    }

    fun loadAuthInfo(): AuthInfo {
        return authInfo
    }

    data class AuthInfo(
        val accessToken: String = "",
        val refreshToken: String = "",
        val userId: Int = 0
    )

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
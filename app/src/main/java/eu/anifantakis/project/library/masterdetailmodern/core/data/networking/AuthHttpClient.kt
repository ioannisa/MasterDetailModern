package eu.anifantakis.project.library.masterdetailmodern.core.data.networking

import eu.anifantakis.lib.securepersist.PersistManager
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataResult
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import kotlinx.serialization.Serializable

class AuthHttpClient(
    tag: String,
    baseUrl: String,
    apiKey: String? = null,
    persistManager: PersistManager
) : CommonHttpClient(tag, baseUrl, apiKey, null) {

    private var accessToken by persistManager.preference("")
    private var refreshToken by persistManager.preference("")
    private var userId by persistManager.preference(0)

    init {
        client.config {
            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(
                            accessToken = accessToken,
                            refreshToken = refreshToken
                        )
                    }

                    refreshTokens {
                        val response = post<AccessTokenRequest, AccessTokenResponse>(
                            route = "/auth/refresh",
                            body = AccessTokenRequest(
                                refreshToken = refreshToken,
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
                            accessToken = accessToken,
                            refreshToken = refreshToken
                        )
                    }
                }
            }
        }
    }

    fun persistAuthInfo(accessToken: String, refreshToken: String, userId: Int? = null) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        if (userId != null) {
            this.userId = userId
        }
    }

    fun loadAuthInfo(): AuthInfo {
        return AuthInfo(accessToken, refreshToken, userId)
    }

    data class AuthInfo(
        val accessToken: String,
        val refreshToken: String,
        val userId: Int
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
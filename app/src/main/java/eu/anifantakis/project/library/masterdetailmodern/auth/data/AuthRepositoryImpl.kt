package eu.anifantakis.project.library.masterdetailmodern.auth.data

import eu.anifantakis.project.library.masterdetailmodern.auth.data.requests.LoginRequest
import eu.anifantakis.project.library.masterdetailmodern.auth.data.requests.LoginResponse
import eu.anifantakis.project.library.masterdetailmodern.auth.data.requests.RegisterWithUsernameRequest
import eu.anifantakis.project.library.masterdetailmodern.auth.domain.AuthRepository
import eu.anifantakis.project.library.masterdetailmodern.core.data.networking.AuthHttpClient
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataError
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.EmptyDataResult
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.asEmptyDataResult
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataResult

class AuthRepositoryImpl(
    private val httpClient: AuthHttpClient
): AuthRepository {

    // https://dummyjson.com/docs/auth

    override suspend fun register(username: String, password: String): EmptyDataResult<DataError.Network> {
        return httpClient.post<RegisterWithUsernameRequest, Unit>(
            route = "/auth/login",
            body = RegisterWithUsernameRequest(
                username = username,
                password = password
            )
        )
    }

    override suspend fun login(username: String, password: String): EmptyDataResult<DataError.Network> {
        val result = httpClient.post<LoginRequest, LoginResponse>(
            route = "/auth/login",
            body = LoginRequest(
                username = username,
                password = password
            )
        )

        if (result is DataResult.Success) {
            httpClient.persistAuthInfo(
                accessToken = result.data.accessToken,
                refreshToken = result.data.refreshToken,
                userId = result.data.userId
            )
        }
        return result.asEmptyDataResult()
    }

    override suspend fun fetchAuthInfo(): AuthHttpClient.AuthInfo {
        return httpClient.loadAuthInfo()
    }
}
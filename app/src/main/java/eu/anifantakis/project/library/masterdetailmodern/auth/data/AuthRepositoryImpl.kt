package eu.anifantakis.project.library.masterdetailmodern.auth.data

import eu.anifantakis.project.library.masterdetailmodern.auth.data.requests.RegisterWithUsernameRequest
import eu.anifantakis.project.library.masterdetailmodern.auth.domain.AuthRepository
import eu.anifantakis.project.library.masterdetailmodern.core.data.networking.AuthHttpClient
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataError
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.EmptyDataResult

class AuthRepositoryImpl(
    private val httpClient: AuthHttpClient
): AuthRepository {
    override suspend fun register(username: String, password: String): EmptyDataResult<DataError.Network> {
        return httpClient.post<RegisterWithUsernameRequest, Unit>(
            route = "/auth/login",
            body = RegisterWithUsernameRequest(
                username = username,
                password = password
            )
        )
    }
}
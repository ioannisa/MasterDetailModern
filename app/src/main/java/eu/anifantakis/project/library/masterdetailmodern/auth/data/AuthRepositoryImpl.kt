package eu.anifantakis.project.library.masterdetailmodern.auth.data

import eu.anifantakis.project.library.masterdetailmodern.auth.data.requests.RegisterRequest
import eu.anifantakis.project.library.masterdetailmodern.auth.domain.AuthRepository
import eu.anifantakis.project.library.masterdetailmodern.core.data.networking.AuthHttpClient
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataError
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.EmptyDataResult

class AuthRepositoryImpl(
    private val httpClient: AuthHttpClient
): AuthRepository {
    override suspend fun register(email: String, password: String): EmptyDataResult<DataError.Network> {
        return httpClient.post<RegisterRequest, Unit>(
            route = "/auth/login",
            body = RegisterRequest(
                email = email,
                password = password
            )
        )
    }
}
package eu.anifantakis.project.library.masterdetailmodern.auth.domain

import eu.anifantakis.project.library.masterdetailmodern.core.data.networking.AuthHttpClient
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataError
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.EmptyDataResult

interface AuthRepository {

    suspend fun register(username: String, password: String): EmptyDataResult<DataError.Network>

    suspend fun login(username: String, password: String): EmptyDataResult<DataError.Network>

    suspend fun fetchAuthInfo(): AuthHttpClient.AuthInfo

}
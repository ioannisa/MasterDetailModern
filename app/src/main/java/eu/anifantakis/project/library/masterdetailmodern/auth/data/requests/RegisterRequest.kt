package eu.anifantakis.project.library.masterdetailmodern.auth.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class RegisterWithUsernameRequest(
    val username: String,
    val password: String
)
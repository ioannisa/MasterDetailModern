package eu.anifantakis.project.library.masterdetailmodern.auth.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String
)
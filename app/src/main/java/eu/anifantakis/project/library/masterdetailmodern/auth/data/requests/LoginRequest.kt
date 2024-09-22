package eu.anifantakis.project.library.masterdetailmodern.auth.data.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class LoginResponse(
    @SerialName("accessToken")
    val accessToken: String,
    val refreshToken: String,
    @SerialName("id")
    val userId: Int
)


// sample response from server

//{
//    "id": 1,
//    "username": "emilys",
//    "email": "emily.johnson@x.dummyjson.com",
//    "firstName": "Emily",
//    "lastName": "Johnson",
//    "gender": "female",
//    "image": "https://dummyjson.com/icon/emilys/128",
//    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", // JWT accessToken (for backward compatibility) in response and cookies
//    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." // refreshToken in response and cookies
//}
package eu.anifantakis.project.library.masterdetailmodern.core.domain.util

interface Error { }

sealed interface DataError : Error {

    enum class Network(family: Int) : DataError {
        // 400 Specific Errors
        REQUEST_TIMEOUT(400),
        BAD_REQUEST(400),
        NOT_FOUND(400),
        METHOD_NOT_ALLOWED(400),
        NOT_ACCEPTABLE(400),
        PROXY_AUTHENTICATION_REQUIRED(400),
        FORBIDDEN(400),
        UNAUTHORIZED(400),
        CONFLICT(400),
        GONE(400),
        TOO_MANY_REQUESTS(400),
        PAYLOAD_TOO_LARGE(400),

        // 500 Specific Errors
        INTERNAL_SERVER_ERROR(500),
        NOT_IMPLEMENTED(500),
        BAD_GATEWAY(500),
        SERVICE_UNAVAILABLE(500),
        GATEWAY_TIMEOUT(500),

        // Generic Errors in 400 and 500 family
        CLIENT_ERROR(400),
        SERVER_ERROR(500),

        // Other Errors
        NO_INTERNET(0),
        SERIALIZATION(0),
        UNKNOWN(0);
    }

    enum class Local: DataError {
        DISK_FULL
    }
}
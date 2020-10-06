package co.nimblehq.data.api.common.secrets

import co.nimblehq.data.service.common.secrets.ApiEndpointUrlImpl
import co.nimblehq.data.service.common.secrets.ClientIdImpl
import co.nimblehq.data.service.common.secrets.ClientSecretImpl

object Secrets {
    val apiEndpointUrl: String
        get() = ApiEndpointUrlImpl().value

    val clientId: String
        get() = ClientIdImpl().value

    val clientSecret: String
        get() = ClientSecretImpl().value
}

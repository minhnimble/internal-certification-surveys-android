package co.nimblehq.data.service.common.secrets

import co.nimblehq.data.api.common.secrets.ApiEndpointUrl

class ApiEndpointUrlImpl : ApiEndpointUrl {
    override val value: String
        get() = "https://nimble-survey-web-staging.herokuapp.com/"
}

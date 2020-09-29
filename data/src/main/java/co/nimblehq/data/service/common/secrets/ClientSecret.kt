package co.nimblehq.data.service.common.secrets

interface ClientSecret {
    companion object {
        const val Secret = ""
    }
    val value: String
}

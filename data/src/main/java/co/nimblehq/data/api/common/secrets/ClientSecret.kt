package co.nimblehq.data.api.common.secrets

interface ClientSecret {
    companion object {
        const val Secret = ""
    }
    val value: String
}

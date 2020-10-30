package co.nimblehq.data.api.response.survey

import com.squareup.moshi.Json

data class AnswerResponse(
    @Json(name = "id") var id: String = "",
    @Json(name = "text") var text: String? = null,
    @Json(name = "display_order") var displayOrder: Int? = null
)

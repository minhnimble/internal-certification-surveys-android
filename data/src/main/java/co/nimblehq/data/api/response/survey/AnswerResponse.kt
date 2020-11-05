package co.nimblehq.data.api.response.survey

import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "answer")
data class AnswerResponse(
    @field: Json(name = "text") var text: String? = null,
    @field: Json(name = "display_order") var displayOrder: Int? = null
): Resource()

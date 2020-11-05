package co.nimblehq.data.api.response.survey

import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasMany
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "question")
data class QuestionResponse(
    @field: Json(name = "text") var text: String? = null,
    @field: Json(name = "display_order") var displayOrder: Int? = null,
    @field: Json(name = "display_type") var displayType: String? = null,
    @field: Json(name = "pick") var pick: String? = null,
    private var answers: HasMany<AnswerResponse>? = null
): Resource() {

    fun getAnswerResponses(): List<AnswerResponse>? {
        return answers?.get(document)
    }
}

package co.nimblehq.data.api.response.survey

import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasMany
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "survey")
data class SurveyDetailsResponse(
    @field: Json(name = "title") var title: String? = null,
    @field: Json(name = "description") var description: String? = null,
    @field: Json(name = "cover_image_url") var coverImageUrl: String? = null,
    private var questions: HasMany<QuestionResponse>? = null,
) : Resource() {

    fun getQuestionResponses(): List<QuestionResponse>? {
        return questions?.get(document)
    }
}

package co.nimblehq.data.api.response.survey

import com.squareup.moshi.Json

data class SurveyResponse(
    @Json(name = "id") var id: String = "",
    @Json(name = "title") var title: String? = null,
    @Json(name = "description") var description: String? = null,
    @Json(name = "cover_image_url") var coverImageUrl: String? = null,
    @Json(name = "questions") var questions: List<QuestionResponse>? = null
)

data class SurveysResponse(
    @Json(name = "page") var page: Int? = null,
    @Json(name = "pages") var pages: Int? = null,
    @Json(name = "surveys") var surveys: List<SurveyResponse>? = null,
)

package co.nimblehq.data.api.response.survey

import com.squareup.moshi.Json

data class SurveyResponse(
    @Json(name = "id") var id: String = "",
    @Json(name = "title") var title: String = "",
    @Json(name = "description") var description: String = "",
    @Json(name = "cover_image_url") var coverImageUrl: String = ""
)

data class SurveysResponse(
    @Json(name = "page") var page: Int = 0,
    @Json(name = "pages") var pages: Int = 0,
    @Json(name = "surveys") var surveys: List<SurveyResponse> = listOf(),
)

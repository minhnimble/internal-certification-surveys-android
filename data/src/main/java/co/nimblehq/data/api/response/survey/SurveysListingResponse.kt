package co.nimblehq.data.api.response.survey

import com.squareup.moshi.Json

data class SurveysListingResponse(
    @Json(name = "data") val data: List<SurveyItemResponse> = listOf(),
    @Json(name = "meta") val meta: SurveysListingMetaResponse
)

data class SurveysListingMetaResponse(
    @Json(name = "page") val page: Int = 0,
    @Json(name = "pages") val pages: Int = 0,
    @Json(name = "page_size") val pageSize: Int = 0,
    @Json(name = "records") val records: Int = 0
)

data class SurveyItemResponse(
    @Json(name = "id") val id: String = "",
    @Json(name = "type") val type: String = "",
    @Json(name = "attributes") val attributes: SurveyItemAttributesResponse,
    @Json(name = "relationships") val relationships: SurveyItemRelationshipsResponse
)

data class SurveyItemAttributesResponse(
    @Json(name = "title") val title: String = "",
    @Json(name = "description") val description: String = "",
    @Json(name = "thank_email_above_threshold") val thankEmailAboveThres: String = "",
    @Json(name = "thank_email_below_threshold") val thankEmailBelowThres: String = "",
    @Json(name = "is_active") val isActive: Boolean = false,
    @Json(name = "cover_image_url") val coverImageUrl: String = "",
    @Json(name = "created_at") val createdAt: String = "",
    @Json(name = "active_at") val activeAt: String = "",
    @Json(name = "inactive_at") val inactiveAt: String? = null,
    @Json(name = "survey_type") val surveyType: String = ""
)

data class SurveyItemRelationshipsResponse(
    @Json(name = "questions") val questions: SurveyItemQuestionsListingResponse
)

data class SurveyItemQuestionsListingResponse(
    @Json(name = "data") val data: List<SurveyItemQuestionResponse> = listOf()
)

data class SurveyItemQuestionResponse(
    @Json(name = "id") val id: String = "",
    @Json(name = "type") val type: String = ""
)

package co.nimblehq.data.api.response.survey

import com.google.gson.annotations.SerializedName

data class SurveysListingResponse(
    @SerializedName("data") val data: List<SurveyItem> = listOf(),
    @SerializedName("meta") val meta: SurveysListingMeta
)

data class SurveysListingMeta(
    @SerializedName("page") val page: Int = 0,
    @SerializedName("pages") val pages: Int = 10,
    @SerializedName("page_size") val pageSize: Int = 0,
    @SerializedName("records") val records: Int = 0
)

data class SurveyItem(
    @SerializedName("id") val id: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("attributes") val attributes: SurveyItemAttributes,
    @SerializedName("relationships") val relationships: List<SurveyItemRelationships> = listOf()
)

data class SurveyItemAttributes(
    @SerializedName("title") val title: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("thank_email_above_threshold") val thankEmailAboveThres: String = "",
    @SerializedName("thank_email_below_threshold") val thankEmailBelowThres: String = "",
    @SerializedName("is_active") val isActive: Boolean = false,
    @SerializedName("cover_image_url") val coverImageUrl: String = "",
    @SerializedName("created_at") val createdAt: String = "",
    @SerializedName("active_at") val activeAt: String = "",
    @SerializedName("inactive_at") val inactiveAt: String? = null,
    @SerializedName("survey_type") val surveyType: String = ""
)

data class SurveyItemRelationships(
    @SerializedName("questions") val questions: SurveyItemQuestionsListing
)

data class SurveyItemQuestionsListing(
    @SerializedName("data") val data: List<SurveyItemQuestion> = listOf()
)

data class SurveyItemQuestion(
    @SerializedName("id") val id: String = "",
    @SerializedName("type") val type: String = ""
)

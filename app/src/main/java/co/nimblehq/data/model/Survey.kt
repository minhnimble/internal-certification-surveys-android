package co.nimblehq.data.model

import co.nimblehq.data.api.response.survey.SurveyResponse

data class Survey(
    val description: String = "",
    val id: String = "",
    val imageUrl: String = "",
    val title: String = ""
) {
    val highResImageUrl: String
        get() = imageUrl + "l"
}

fun SurveyResponse.toSurvey() = Survey(
    description = description,
    id = id,
    imageUrl = coverImageUrl,
    title = title
)

fun List<SurveyResponse>.toSurveys() = this.map { it.toSurvey() }

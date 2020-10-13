package co.nimblehq.data.model

import co.nimblehq.data.api.response.survey.SurveysListingResponse

data class Survey(
    val description: String = "",
    val id: String = "",
    val imageUrl: String = "",
    val title: String = ""
) {
    val highResImageUrl: String
        get() = imageUrl + "l"
}

fun SurveysListingResponse.toSurveys() = with(data) {
    map {
        Survey(
            it.attributes.description,
            it.id,
            it.attributes.coverImageUrl,
            it.attributes.title
        )
    }
}

package co.nimblehq.ui.screen.main.surveys.adapter

import co.nimblehq.data.model.Survey

data class SurveysPagerItemUiModel(
    val id: String = "",
    val description: String = "",
    val header: String = "",
    val imageUrl: String = ""
)

fun Survey.toSurveysPagerItemUiModel() = SurveysPagerItemUiModel(
    id,
    description,
    title,
    highResImageUrl
)

package co.nimblehq.ui.screen.main.surveys

import co.nimblehq.data.model.Survey

data class SurveyItemUiModel(
    val id: String = "",
    val description: String = "",
    val header: String = "",
    val imageUrl: String = ""
)

fun Survey.toSurveyItemUiModel() = SurveyItemUiModel(
    id,
    description,
    title,
    highResImageUrl
)

fun List<Survey>.toSurveyItemUiModels() = map { it.toSurveyItemUiModel() }

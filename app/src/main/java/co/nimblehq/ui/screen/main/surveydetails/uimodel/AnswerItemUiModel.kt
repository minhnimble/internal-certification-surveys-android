package co.nimblehq.ui.screen.main.surveydetails.uimodel

import co.nimblehq.data.model.Answer

data class AnswerItemUiModel(
    val id: String,
    var text: String
)

fun Answer.toAnswerItemUiModel() = AnswerItemUiModel(
    id,
    text
)

fun List<Answer>.toAnswerItemUiModels() = this.map { it.toAnswerItemUiModel() }

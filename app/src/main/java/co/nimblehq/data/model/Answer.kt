package co.nimblehq.data.model

import co.nimblehq.data.api.response.survey.AnswerResponse

data class Answer(
    val id: String = "",
    val text: String = "",
    val displayOrder: Int = -1
)

fun AnswerResponse.toAnswer() = Answer(
    id = id,
    text = text.orEmpty(),
    displayOrder = displayOrder ?: -1
)

fun List<AnswerResponse>.toAnswers() = this.map { it.toAnswer() }

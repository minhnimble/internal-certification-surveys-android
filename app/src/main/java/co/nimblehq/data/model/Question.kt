package co.nimblehq.data.model

import co.nimblehq.data.api.response.survey.QuestionDisplayType
import co.nimblehq.data.api.response.survey.QuestionPickValue
import co.nimblehq.data.api.response.survey.QuestionResponse

data class Question(
    var id: String = "",
    var text: String = "",
    var displayOrder: Int = -1,
    var displayType: QuestionDisplayType = QuestionDisplayType.DEFAULT,
    var pick: QuestionPickValue = QuestionPickValue.NONE,
    val answers: List<Answer> = listOf()
)

fun QuestionResponse.toQuestion() = Question(
    id = id,
    text = text.orEmpty(),
    displayOrder = displayOrder ?: -1,
    displayType = displayType ?: QuestionDisplayType.DEFAULT,
    pick = pick ?: QuestionPickValue.NONE,
    answers = answers?.toAnswers() ?: emptyList()
)

fun List<QuestionResponse>.toQuestions() = this.map { it.toQuestion() }

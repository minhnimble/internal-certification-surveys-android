package co.nimblehq.ui.screen.main.surveydetails

import co.nimblehq.data.model.Question
import co.nimblehq.data.model.QuestionDisplayType
import co.nimblehq.data.model.QuestionPickValue
import co.nimblehq.data.model.Survey

data class QuestionItemPagerUiModel(
    val id: String,
    val text: String,
    val displayType: QuestionDisplayType,
    val pick: QuestionPickValue,
    val answers: List<AnswerItemUiModel>
)

fun Question.toQuestionItemPagerUiModel() =
    QuestionItemPagerUiModel(
        id,
        text,
        displayType,
        pick,
        answers.sortedBy { it.displayOrder }.toAnswerItemUiModels()
    )

fun List<Question>.toQuestionItemPagerUiModels() = this.map { it.toQuestionItemPagerUiModel() }

fun Survey.toQuestionItemPagerUiModels() = questions.toQuestionItemPagerUiModels()

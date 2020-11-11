package co.nimblehq.ui.screen.main.surveydetails.uimodel

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
) {
    val shouldAnswer: Boolean
        get() {
            return displayType != QuestionDisplayType.INTRO &&
                displayType != QuestionDisplayType.OUTRO &&
                displayType != QuestionDisplayType.DEFAULT &&
                answers.isNotEmpty()
        }
}

fun Question.toQuestionItemPagerUiModel() =
    QuestionItemPagerUiModel(
        id,
        text,
        displayType,
        pick,
        answers.sortedBy { it.displayOrder }.toAnswerItemUiModels()
    )

fun List<Question>.toQuestionItemPagerUiModels() = this.map { it.toQuestionItemPagerUiModel() }

fun Survey.toQuestionItemPagerUiModels() = questions.sortedBy { it.displayOrder }.toQuestionItemPagerUiModels()

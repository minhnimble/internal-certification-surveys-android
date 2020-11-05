package co.nimblehq.data.model

import co.nimblehq.data.api.response.survey.QuestionResponse

enum class QuestionDisplayType {
    INTRO,
    STAR,
    HEART,
    SMILEY,
    CHOICE,
    NPS,
    TEXTAREA,
    TEXTFIELD,
    OUTRO,
    DEFAULT;

    companion object {
        fun from(value: String?): QuestionDisplayType {
            value ?: return DEFAULT
            return try {
                valueOf(value.toUpperCase())
            } catch (ex: Exception) {
                DEFAULT
            }
        }
    }
}

enum class QuestionPickValue {
    ONE,
    ANY,
    NONE;

    companion object {
        fun from(value: String?): QuestionPickValue {
            value ?: return NONE
            return try {
                valueOf(value.toUpperCase())
            } catch (ex: Exception) {
                NONE
            }
        }
    }
}

data class Question(
    var id: String = "",
    var text: String = "",
    var displayOrder: Int = -1,
    var displayType: QuestionDisplayType = QuestionDisplayType.DEFAULT,
    var pick: QuestionPickValue = QuestionPickValue.NONE,
    val answers: List<Answer> = listOf()
)

fun QuestionResponse.toQuestion() = Question(
    id = id.orEmpty(),
    text = text.orEmpty(),
    displayOrder = displayOrder ?: -1,
    displayType = QuestionDisplayType.from(displayType),
    pick = QuestionPickValue.from(pick),
    answers = getAnswerResponses()?.toAnswers() ?: emptyList()
)

fun List<QuestionResponse>.toQuestions() = this.map { it.toQuestion() }

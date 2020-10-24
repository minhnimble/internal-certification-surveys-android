package co.nimblehq.data.model

import androidx.room.*
import co.nimblehq.data.api.response.survey.QuestionDisplayType
import co.nimblehq.data.api.response.survey.QuestionPickValue
import co.nimblehq.data.api.response.survey.QuestionResponse
import co.nimblehq.data.storage.dao.converter.QuestionDisplayTypeConverter
import co.nimblehq.data.storage.dao.converter.QuestionPickValueConverter

@TypeConverters(
    QuestionDisplayTypeConverter::class,
    QuestionPickValueConverter::class
)
data class Question(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: String = "",
    @ColumnInfo(name = "text") var text: String = "",
    @ColumnInfo(name = "display_order") var displayOrder: Int = -1,
    @ColumnInfo(name = "display_type") var displayType: QuestionDisplayType = QuestionDisplayType.DEFAULT,
    @ColumnInfo(name = "pick") var pick: QuestionPickValue = QuestionPickValue.NONE,
    @Ignore val answers: List<Answer> = listOf()
)

fun QuestionResponse.toQuestion() = Question(
    id = id,
    text = text,
    displayOrder = displayOrder,
    displayType = displayType,
    pick = pick,
    answers = answers.toAnswers()
)

fun List<QuestionResponse>.toQuestions() = this.map { it.toQuestion() }

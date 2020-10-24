package co.nimblehq.data.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import co.nimblehq.data.api.response.survey.AnswerResponse

data class Answer(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String = "",
    @ColumnInfo(name = "text") val text: String = "",
    @ColumnInfo(name = "display_order") val displayOrder: Int = -1
)

fun AnswerResponse.toAnswer() = Answer(
    id = id,
    text = text,
    displayOrder = displayOrder
)

fun List<AnswerResponse>.toAnswers() = this.map { it.toAnswer() }

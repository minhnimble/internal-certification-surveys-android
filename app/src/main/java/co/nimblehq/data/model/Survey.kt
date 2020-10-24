package co.nimblehq.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import co.nimblehq.data.api.response.survey.SurveyResponse

@Entity(tableName = "survey")
data class Survey(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "cover_image_url") var imageUrl: String = "",
    @ColumnInfo(name = "title") var title: String = "",
    @Ignore var questions: List<Question> = listOf()
) {
    val highResImageUrl: String
        get() = imageUrl + "l"
}

fun SurveyResponse.toSurvey() = Survey(
    description = description,
    id = id,
    imageUrl = coverImageUrl,
    title = title,
    questions = questions.toQuestions()
)

fun List<SurveyResponse>.toSurveys() = this.map { it.toSurvey() }

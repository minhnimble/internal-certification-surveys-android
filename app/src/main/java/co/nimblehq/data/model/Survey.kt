package co.nimblehq.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import co.nimblehq.data.api.response.survey.SurveyBasicResponse

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

fun SurveyBasicResponse.toSurvey() = Survey(
    description = description.orEmpty(),
    id = id.orEmpty(),
    imageUrl = coverImageUrl.orEmpty(),
    title = title.orEmpty()
)

fun List<SurveyBasicResponse>.toSurveys() = this.map { it.toSurvey() }

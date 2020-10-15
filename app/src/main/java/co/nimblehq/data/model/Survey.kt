package co.nimblehq.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import co.nimblehq.data.api.response.survey.SurveyResponse
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "survey")
@Parcelize
data class Survey(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String = "",
    @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "cover_image_url") val imageUrl: String = "",
    @ColumnInfo(name = "title") val title: String = ""
) : Parcelable {
    val highResImageUrl: String
        get() = imageUrl + "l"
}

fun SurveyResponse.toSurvey() = Survey(
    description = description,
    id = id,
    imageUrl = coverImageUrl,
    title = title
)

fun List<SurveyResponse>.toSurveys() = this.map { it.toSurvey() }

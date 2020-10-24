package co.nimblehq.data.api.response.survey

import androidx.annotation.Keep
import com.squareup.moshi.Json

data class SurveyResponse(
    @Json(name = "id") var id: String = "",
    @Json(name = "title") var title: String = "",
    @Json(name = "description") var description: String = "",
    @Json(name = "cover_image_url") var coverImageUrl: String = "",
    @Json(name = "questions") var questions: List<QuestionResponse> = listOf()
)

data class SurveysResponse(
    @Json(name = "page") var page: Int = 0,
    @Json(name = "pages") var pages: Int = 0,
    @Json(name = "surveys") var surveys: List<SurveyResponse> = listOf(),
)

data class QuestionResponse(
    @Json(name = "id") var id: String = "",
    @Json(name = "text") var text: String = "",
    @Json(name = "display_order") var displayOrder: Int = -1,
    @Json(name = "display_type") var displayType: QuestionDisplayType = QuestionDisplayType.DEFAULT,
    @Json(name = "pick") var pick: QuestionPickValue = QuestionPickValue.NONE,
    @Json(name = "answers") var answers: List<AnswerResponse> = listOf()
)

data class AnswerResponse(
    @Json(name = "id") var id: String = "",
    @Json(name = "text") var text: String = "",
    @Json(name = "display_order") var displayOrder: Int = -1
)

@Keep
enum class QuestionDisplayType {
    INTRO,
    STAR,
    HEART,
    SMILEY,
    CHOICE,
    NPS,
    TEXTAREA,
    TEXTFIELD,
    OUTTRO,
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

@Keep
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

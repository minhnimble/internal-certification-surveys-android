package co.nimblehq.data.api.response.survey

import androidx.annotation.Keep
import com.squareup.moshi.Json

data class QuestionResponse(
    @Json(name = "id") var id: String = "",
    @Json(name = "text") var text: String? = null,
    @Json(name = "display_order") var displayOrder: Int? = null,
    @Json(name = "display_type") var displayType: QuestionDisplayType? = null,
    @Json(name = "pick") var pick: QuestionPickValue? = null,
    @Json(name = "answers") var answers: List<AnswerResponse>? = null
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

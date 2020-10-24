package co.nimblehq.data.storage.dao.converter

import androidx.room.TypeConverter
import co.nimblehq.data.api.response.survey.QuestionDisplayType

class QuestionDisplayTypeConverter {

    @TypeConverter
    fun toQuestionDisplayType(value: String): QuestionDisplayType {
        return QuestionDisplayType.from(value)
    }

    @TypeConverter
    fun fromQuestionDisplayType(type: QuestionDisplayType): String {
        return type.name
    }
}

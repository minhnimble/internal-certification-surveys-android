package co.nimblehq.data.storage.dao.converter

import androidx.room.TypeConverter
import co.nimblehq.data.api.response.survey.QuestionDisplayType
import co.nimblehq.data.api.response.survey.QuestionPickValue

class QuestionPickValueConverter {

    @TypeConverter
    fun toQuestionPickValue(value: String): QuestionPickValue {
        return QuestionPickValue.from(value)
    }

    @TypeConverter
    fun fromQuestionPickValue(type: QuestionPickValue): String {
        return type.name
    }
}

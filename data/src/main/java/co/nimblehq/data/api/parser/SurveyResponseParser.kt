package co.nimblehq.data.api.parser

import co.nimblehq.data.api.response.survey.SurveyResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

class SurveyResponseParser: JsonAdapter<SurveyResponse>() {

    override fun fromJson(reader: JsonReader): SurveyResponse {
        reader.isLenient = true
        val surveyResponse = SurveyResponse()

        return try {
            reader.readObject {
                when (reader.nextName()) {
                    "id" -> surveyResponse.id = reader.nextString()
                    "attributes" -> {
                        reader.readObject {
                            when (reader.nextName()) {
                                "title" -> surveyResponse.title = reader.nextString()
                                "description" -> surveyResponse.description = reader.nextString()
                                "cover_image_url" -> surveyResponse.coverImageUrl = reader.nextString()
                                else -> reader.skipValue()
                            }
                        }
                    }
                    else -> reader.skipValue()
                }
            }

            surveyResponse
        } catch (e: Exception) {
            surveyResponse
        }
    }

    override fun toJson(writer: JsonWriter, value: SurveyResponse?) { }
}

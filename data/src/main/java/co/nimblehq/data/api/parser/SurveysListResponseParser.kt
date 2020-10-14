package co.nimblehq.data.api.parser

import co.nimblehq.data.api.response.survey.SurveyResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

class SurveysListResponseParser: JsonAdapter<List<SurveyResponse>>() {

    override fun fromJson(reader: JsonReader): List<SurveyResponse> {
        reader.isLenient = true
        var surveysListResponse = listOf<SurveyResponse>()

        return try {
            reader.readObject {
                when(reader.nextName()) {
                    "data" -> {
                        surveysListResponse = reader.readArrayToList {
                            val surveyResponse = SurveyResponse()
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
                        }
                    }
                    else -> reader.skipValue()
                }
            }

            surveysListResponse
        } catch (e: Exception) {
            surveysListResponse
        }
    }

    override fun toJson(writer: JsonWriter, value: List<SurveyResponse>?) { }
}


package co.nimblehq.data.api.parser

import co.nimblehq.data.api.response.survey.SurveyResponse
import co.nimblehq.data.api.response.survey.SurveysResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

class SurveysResponseParser: JsonAdapter<SurveysResponse>() {

    override fun fromJson(reader: JsonReader): SurveysResponse {
        reader.isLenient = true
        val surveysResponse = SurveysResponse()
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
                    "meta" -> {
                        reader.readObject {
                            when (reader.nextName()) {
                                "pages" -> surveysResponse.pages = reader.nextInt()
                                else -> reader.skipValue()
                            }
                        }
                    }
                    else -> reader.skipValue()
                }
            }

            surveysResponse.surveys = surveysListResponse
            surveysResponse
        } catch (e: Exception) {
            surveysResponse
        }
    }

    override fun toJson(writer: JsonWriter, value: SurveysResponse?) { }
}


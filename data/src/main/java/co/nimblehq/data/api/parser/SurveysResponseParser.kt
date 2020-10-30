package co.nimblehq.data.api.parser

import co.nimblehq.data.api.response.survey.SurveyResponse
import co.nimblehq.data.api.response.survey.SurveysResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

// TODO: Improve this parser with `Banana` for properly parsing `json api` instead of manual parsing like now
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
                                                "title" -> surveyResponse.title = reader.nextStringOrNull()
                                                "description" -> surveyResponse.description = reader.nextStringOrNull()
                                                "cover_image_url" -> surveyResponse.coverImageUrl = reader.nextStringOrNull()
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
                                "page" -> surveysResponse.page = reader.nextIntOrNull()
                                "pages" -> surveysResponse.pages = reader.nextIntOrNull()
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


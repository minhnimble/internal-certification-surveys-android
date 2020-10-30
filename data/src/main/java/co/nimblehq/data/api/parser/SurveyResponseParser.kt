package co.nimblehq.data.api.parser

import co.nimblehq.data.api.response.survey.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

// TODO: Improve this parser with `Banana` for properly parsing `json api` instead of manual parsing like now
class SurveyResponseParser: JsonAdapter<SurveyResponse>() {

    override fun fromJson(reader: JsonReader): SurveyResponse {
        reader.isLenient = true
        val surveyResponse = SurveyResponse()
        val allQuestionsResponse = mutableListOf<QuestionResponse>()
        val allAnswersResponse = mutableListOf<AnswerResponse>()

        return try {
            reader.readObject {
                when (reader.nextName()) {
                    "id" -> surveyResponse.id = reader.nextString()
                    "included" -> {
                        reader.readArray {
                            var id = ""
                            var type = ""
                            val questionResponse = QuestionResponse()
                            reader.readObject {
                                when (reader.nextName()) {
                                    "id" -> id = reader.nextString()
                                    "type" -> reader.nextStringOrNull()?.let { type = it }
                                    "attributes" -> {
                                        if (type == "question") {
                                            questionResponse.id = id
                                            reader.readObject {
                                                when (reader.nextName()) {
                                                    "text" -> questionResponse.text = reader.nextStringOrNull()
                                                    "display_order" -> questionResponse.displayOrder = reader.nextIntOrNull()
                                                    "display_type" -> questionResponse.displayType = QuestionDisplayType.from(reader.nextStringOrNull())
                                                    "pick" -> questionResponse.pick = QuestionPickValue.from(reader.nextStringOrNull())
                                                    else -> reader.skipValue()
                                                }
                                            }
                                            allQuestionsResponse.add(questionResponse)
                                        } else if (type == "answer") {
                                            val answerResponse = AnswerResponse()
                                            answerResponse.id = id
                                            reader.readObject {
                                                when (reader.nextName()) {
                                                    "text" -> answerResponse.text = reader.nextStringOrNull()
                                                    "display_order" -> answerResponse.displayOrder = reader.nextIntOrNull()
                                                    else -> reader.skipValue()
                                                }
                                            }
                                            allAnswersResponse.add(answerResponse)
                                        }
                                    }
                                    "relationships" -> {
                                        reader.readObject {
                                            when (reader.nextName()) {
                                                "answers" -> {
                                                    reader.readObject {
                                                        when (reader.nextName()) {
                                                            "data" -> {
                                                                questionResponse.answers = reader.readArrayToList {
                                                                    val answerResponse = AnswerResponse()
                                                                    reader.readObject {
                                                                        when (reader.nextName()) {
                                                                            "id" -> answerResponse.id = reader.nextString()
                                                                            else -> reader.skipValue()
                                                                        }
                                                                    }
                                                                    answerResponse
                                                                }

                                                                // Update with full question response
                                                                val index = allQuestionsResponse.indexOfFirst { it.id == questionResponse.id }
                                                                if (index == -1) {
                                                                    allQuestionsResponse.add(questionResponse)
                                                                } else {
                                                                    allQuestionsResponse[index] = questionResponse
                                                                }
                                                            }
                                                            else -> reader.skipValue()
                                                        }
                                                    }
                                                }
                                                else -> reader.skipValue()
                                            }
                                        }
                                    }
                                    else -> reader.skipValue()
                                }
                            }
                        }
                    }
                    else -> reader.skipValue()
                }
            }

            // Update question's answers list with full answer response
            allQuestionsResponse.forEach {
                val newAnswers = mutableListOf<AnswerResponse>()
                val answerIds = it.answers?.map { answer -> answer.id }
                if (answerIds != null) {
                    for (answerId in answerIds) {
                        allAnswersResponse.firstOrNull { answer -> answer.id == answerId }?.let { answer ->
                            newAnswers.add(answer)
                        }
                    }
                }
                if (newAnswers.isNotEmpty()) it.answers = newAnswers
            }
            surveyResponse.questions = allQuestionsResponse

            surveyResponse
        } catch (e: Exception) {
            surveyResponse
        }
    }

    override fun toJson(writer: JsonWriter, value: SurveyResponse?) { }
}

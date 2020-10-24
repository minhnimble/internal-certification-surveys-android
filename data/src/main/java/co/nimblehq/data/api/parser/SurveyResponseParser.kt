package co.nimblehq.data.api.parser

import co.nimblehq.data.api.response.survey.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

class SurveyResponseParser: JsonAdapter<SurveyResponse>() {

    override fun fromJson(reader: JsonReader): SurveyResponse {
        reader.isLenient = true
        val surveyResponse = SurveyResponse()
        val allQuestionsResponse = mutableListOf<QuestionResponse>()
        val allAnswersResponse = mutableListOf<AnswerResponse>()

        return try {
            reader.readObject {
                when (reader.nextName()) {
                    "id" -> surveyResponse.id = reader.nextStringOrEmpty()
                    "included" -> {
                        reader.readArray {
                            var id = ""
                            var type = ""
                            val questionResponse = QuestionResponse()
                            reader.readObject {
                                when (reader.nextName()) {
                                    "id" -> id = reader.nextStringOrEmpty()
                                    "type" -> type = reader.nextStringOrEmpty()
                                    "attributes" -> {
                                        if (type == "question") {
                                            questionResponse.id = id
                                            reader.readObject {
                                                when (reader.nextName()) {
                                                    "text" -> questionResponse.text = reader.nextStringOrEmpty()
                                                    "display_order" -> questionResponse.displayOrder = reader.nextIntOrEmpty()
                                                    "display_type" -> questionResponse.displayType = QuestionDisplayType.from(reader.nextStringOrEmpty())
                                                    "pick" -> questionResponse.pick = QuestionPickValue.from(reader.nextStringOrEmpty())
                                                    else -> reader.skipValue()
                                                }
                                            }
                                            allQuestionsResponse.add(questionResponse)
                                        } else if (type == "answer") {
                                            val answerResponse = AnswerResponse()
                                            answerResponse.id = id
                                            reader.readObject {
                                                when (reader.nextName()) {
                                                    "text" -> answerResponse.text = reader.nextStringOrEmpty()
                                                    "display_order" -> answerResponse.displayOrder = reader.nextIntOrEmpty()
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
                                                                            "id" -> answerResponse.id = reader.nextStringOrEmpty()
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
                for (answerId in it.answers.map { answer -> answer.id }) {
                    allAnswersResponse.firstOrNull { answer -> answer.id == answerId }?.let { answer ->
                        newAnswers.add(answer)
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

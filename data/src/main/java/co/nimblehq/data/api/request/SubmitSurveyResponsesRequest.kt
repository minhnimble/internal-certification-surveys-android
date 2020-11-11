package co.nimblehq.data.api.request

import com.squareup.moshi.Json

data class SubmitSurveyResponsesRequest(
    @Json(name = "survey_id") val surveyId: String,
    @Json(name = "questions") var questions: List<QuestionResponsesRequest>
)

data class QuestionResponsesRequest(
    @Json(name = "id") val id: String,
    @Json(name = "answers") var answers: List<AnswerRequest>
)

data class AnswerRequest(
    @Json(name = "id") val id: String,
    @Json(name = "answer") var answer: String? = null
)

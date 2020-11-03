package co.nimblehq.data.api.request

import com.squareup.moshi.Json

data class SubmitSurveyRequest(
    @Json(name = "survey_id") val surveyId: String,
    @Json(name = "questions") var questions: List<QuestionRequest>
)

data class QuestionRequest(
    @Json(name = "id") val id: String,
    @Json(name = "answers") var answers: List<AnswerRequest>
)

data class AnswerRequest(
    @Json(name = "id") val id: String,
    @Json(name = "answer") var answer: String? = null
)

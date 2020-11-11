package co.nimblehq.data.model

import co.nimblehq.data.api.request.AnswerRequest
import co.nimblehq.data.api.request.QuestionResponsesRequest

data class QuestionResponsesEntity(
    val id: String,
    var answers: List<AnswerEntity>
)

data class AnswerEntity(
    val id: String,
    var answer: String? = null
)

fun QuestionResponsesEntity.toQuestionRequest() = QuestionResponsesRequest(
    id = id,
    answers = answers.toAnswerRequests()
)

fun List<QuestionResponsesEntity>.toQuestionRequests() = this.map { it.toQuestionRequest() }

fun AnswerEntity.toAnswerRequest() = AnswerRequest(
    id = id,
    answer = answer
)

fun List<AnswerEntity>.toAnswerRequests() = this.map { it.toAnswerRequest() }

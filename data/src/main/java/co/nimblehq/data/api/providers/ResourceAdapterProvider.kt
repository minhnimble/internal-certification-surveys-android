package co.nimblehq.data.api.providers

import co.nimblehq.data.api.response.survey.AnswerResponse
import co.nimblehq.data.api.response.survey.QuestionResponse
import co.nimblehq.data.api.response.survey.SurveyDetailsResponse
import co.nimblehq.data.api.response.user.UserResponse
import moe.banana.jsonapi2.ResourceAdapterFactory

class ResourceAdapterProvider {
    val resourceFactory: ResourceAdapterFactory
        get() = ResourceAdapterFactory
            .builder()
            .add(
                SurveyDetailsResponse::class.java,
                QuestionResponse::class.java,
                AnswerResponse::class.java,
                UserResponse::class.java
            )
            .build()
}

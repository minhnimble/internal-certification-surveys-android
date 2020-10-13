package co.nimblehq.data.repository

import co.nimblehq.data.api.service.survey.SurveyService
import co.nimblehq.data.model.Survey
import co.nimblehq.data.model.toSurveys
import io.reactivex.Single
import javax.inject.Inject

interface SurveyRepository {

    fun getSurveysList(
        pageNumber: Int = 0,
        pageSize: Int
    ): Single<List<Survey>>
}

class SurveyRepositoryImpl @Inject constructor(
    private val surveyService: SurveyService
) : SurveyRepository {

    override fun getSurveysList(
        pageNumber: Int,
        pageSize: Int
    ): Single<List<Survey>> {
        return surveyService
            .getSurveysList(pageNumber, pageSize)
            .firstOrError()
            .map { it.toSurveys() }
    }
}

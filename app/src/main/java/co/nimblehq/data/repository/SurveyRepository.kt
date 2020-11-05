package co.nimblehq.data.repository

import co.nimblehq.data.api.operators.Operators
import co.nimblehq.data.api.response.survey.meta
import co.nimblehq.data.api.service.survey.SurveyService
import co.nimblehq.data.model.Question
import co.nimblehq.data.model.Survey
import co.nimblehq.data.model.toQuestions
import co.nimblehq.data.model.toSurveys
import co.nimblehq.data.storage.AppPreferences
import co.nimblehq.data.storage.dao.SurveyDao
import io.reactivex.Single
import javax.inject.Inject

interface SurveyRepository {

    fun loadSurveyDetails(
        surveyId: String
    ): Single<List<Question>>
    
    fun loadSurveys(
        pageNumber: Int,
        pageSize: Int
    ): Single<List<Survey>>
}

class SurveyRepositoryImpl @Inject constructor(
    private val appPreferences: AppPreferences,
    private val surveyDao: SurveyDao,
    private val surveyService: SurveyService
) : SurveyRepository {

    override fun loadSurveys(
        pageNumber: Int,
        pageSize: Int
    ): Single<List<Survey>> {
        return surveyService
            .getSurveysList(pageNumber, pageSize)
            .lift(Operators.apiError())
            .firstOrError()
            .doOnSuccess {
                appPreferences.surveysCurrentPage = it.meta?.page ?: 1
                appPreferences.surveysTotalPages = it.meta?.pages ?: 1
            }
            .map { it.toSurveys() }
            .doOnSuccess { surveyDao.insertAll(it) }
    }

    override fun loadSurveyDetails(
        surveyId: String
    ): Single<List<Question>> {
        return surveyService
            .getSurveyDetails(surveyId)
            .lift(Operators.apiError())
            .firstOrError()
            .map { it.getQuestionResponses()?.toQuestions() ?: emptyList() }
    }
}

package co.nimblehq.data.repository

import co.nimblehq.data.api.service.survey.SurveyService
import co.nimblehq.data.model.Survey
import co.nimblehq.data.model.toSurveys
import co.nimblehq.data.storage.AppPreferences
import co.nimblehq.data.storage.dao.SurveyDao
import io.reactivex.Single
import javax.inject.Inject

interface SurveyRepository {

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
            .firstOrError()
            .doOnSuccess { appPreferences.surveysTotalPages = it.pages }
            .map { it.surveys.toSurveys() }
            .doOnSuccess { surveyDao.insertAll(it) }
    }
}

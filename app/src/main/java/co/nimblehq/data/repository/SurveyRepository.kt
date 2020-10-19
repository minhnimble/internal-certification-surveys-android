package co.nimblehq.data.repository

import co.nimblehq.data.api.service.survey.SurveyService
import co.nimblehq.data.lib.common.DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER
import co.nimblehq.data.model.Survey
import co.nimblehq.data.model.toSurveys
import co.nimblehq.data.storage.dao.SurveyDao
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

interface SurveyRepository {

    fun getInitialSurveysListWithCache(
        pageNumber: Int = DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER,
        pageSize: Int
    ): Flowable<List<Survey>>

    fun loadMoreSurveysList(
        pageNumber: Int,
        pageSize: Int
    ): Single<List<Survey>>
}

class SurveyRepositoryImpl @Inject constructor(
    private val surveyDao: SurveyDao,
    private val surveyService: SurveyService
) : SurveyRepository {

    override fun getInitialSurveysListWithCache(
        pageNumber: Int,
        pageSize: Int
    ): Flowable<List<Survey>> {
        return Single.concat(
            surveyDao.getAllSurveys(),
            surveyService
            .getSurveysList(pageNumber, pageSize)
            .firstOrError()
            .map { it.toSurveys() }
            .doOnSuccess { surveyDao.insertAll(it) }
        )
    }

    override fun loadMoreSurveysList(
        pageNumber: Int,
        pageSize: Int
    ): Single<List<Survey>> {
        return surveyService
            .getSurveysList(pageNumber, pageSize)
            .firstOrError()
            .map { it.toSurveys() }
            .doOnSuccess { surveyDao.insertAll(it) }
    }
}

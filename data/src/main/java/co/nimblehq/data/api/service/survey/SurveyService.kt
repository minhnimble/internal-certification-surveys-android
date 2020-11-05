package co.nimblehq.data.api.service.survey

import co.nimblehq.data.api.response.survey.SurveyBasicResponse
import co.nimblehq.data.api.response.survey.SurveyDetailsResponse
import co.nimblehq.data.lib.common.REQUEST_KEY_PAGE_NUMBER
import co.nimblehq.data.lib.common.REQUEST_KEY_PAGE_SIZE
import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Providing APIs to communicate with Nimble Surveys for OAuth services
 */
interface SurveyService {

    @GET("/api/v1/surveys")
    fun getSurveysList(
        @Query(REQUEST_KEY_PAGE_NUMBER) pageNumber: Int,
        @Query(REQUEST_KEY_PAGE_SIZE) pageSize: Int
    ): Flowable<Response<List<SurveyBasicResponse>>>

    @GET("/api/v1/surveys/{surveyId}")
    fun getSurveyDetails(
        @Path("surveyId") surveyId: String
    ): Flowable<Response<SurveyDetailsResponse>>
}

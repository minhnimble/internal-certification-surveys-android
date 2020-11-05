package co.nimblehq.data.api.service.user

import co.nimblehq.data.api.response.user.UserResponse
import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.GET


/**
 * Providing APIs to communicate with Nimble Surveys for OAuth services
 */
interface UserService {

    @GET("/api/v1/me")
    fun loadCurrentUserInfo(): Flowable<Response<UserResponse>>
}

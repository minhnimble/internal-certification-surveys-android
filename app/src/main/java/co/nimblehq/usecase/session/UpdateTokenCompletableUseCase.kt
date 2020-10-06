package co.nimblehq.usecase.session

import co.nimblehq.data.error.Ignored
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.service.response.OAuthResponse
import co.nimblehq.data.storage.SecureStorage
import co.nimblehq.usecase.base.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class UpdateTokenCompletableUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val secureStorage: SecureStorage
) : CompletableUseCase<UpdateTokenCompletableUseCase.Input>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.main(),
    ::Ignored
) {

    data class Input(
        val response: OAuthResponse
    )

    override fun create(input: Input): Completable {
        return Completable.fromAction {
            secureStorage.apply {
                userAccessToken = input.response?.data?.attributes?.accessToken
                userAccessTokenCreatedAt = input.response?.data?.attributes?.createdAt
                userAccessTokenExpiresIn = input.response?.data?.attributes?.expiresIn
                userRefreshToken = input.response?.data?.attributes?.refreshToken
                userTokenType = input.response?.data?.attributes?.tokenType
            }
        }
    }
}

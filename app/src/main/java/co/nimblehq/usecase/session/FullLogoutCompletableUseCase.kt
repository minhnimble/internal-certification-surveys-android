package co.nimblehq.usecase.session

import co.nimblehq.data.error.SessionError.LogoutError
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.usecase.base.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class FullLogoutCompletableUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val getLocalUserTokenSingleUseCase: GetLocalUserTokenSingleUseCase,
    private val logoutCompletableUseCase: LogoutCompletableUseCase,
    private val clearLocalTokenCompletableUseCase: ClearLocalTokenCompletableUseCase
) : CompletableUseCase<Unit>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.main(),
    ::LogoutError
) {

    override fun create(input: Unit): Completable {
        return getLocalUserTokenSingleUseCase
            .execute(Unit)
            .flatMapCompletable { logoutCompletableUseCase.execute(it.accessToken) }
            .andThen(clearLocalTokenCompletableUseCase.execute(Unit))
    }
}

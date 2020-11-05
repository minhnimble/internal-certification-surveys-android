package co.nimblehq.usecase.user

import co.nimblehq.data.error.UserError
import co.nimblehq.data.lib.schedulers.TestRxSchedulerProviderImpl
import co.nimblehq.data.model.User
import co.nimblehq.data.repository.UserRepository
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class LoadSurveysSingleUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var useCase: LoadCurrentUserInfoSingleUseCase

    @Before
    fun setUp() {
        userRepository = mock()
        useCase = LoadCurrentUserInfoSingleUseCase(
            TestRxSchedulerProviderImpl(),
            userRepository
        )
    }

    @Test
    fun `When loading current user info from backend successfully, it returns a user`() {
        // Arrange
        whenever(
            userRepository.loadCurrentUserInfo()
        ) doReturn Single.just(User(id = "id", email = "test@gmail.com", avatarUrl = "avatarUrl"))

        // Act
        val positiveTestSubscriber = useCase
            .execute(Unit)
            .test()

        // Assert
        positiveTestSubscriber
            .assertNoErrors()
            .assertValueCount(1)
    }

    @Test
    fun `When current user info from backend failed, it returns GetCurrentUserInfoError`() {
        // Arrange
        whenever(
            userRepository.loadCurrentUserInfo()
        ) doReturn Single.error(UserError.LoadCurrentUserInfoError(null))

        // Act
        val negativeTestSubscriber = useCase
            .execute(Unit)
            .test()

        // Assert
        negativeTestSubscriber
            .assertValueCount(0)
            .assertError { it is UserError.LoadCurrentUserInfoError }
    }
}

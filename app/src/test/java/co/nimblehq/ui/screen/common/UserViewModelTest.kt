package co.nimblehq.ui.screen.common

import co.nimblehq.data.error.UserError
import co.nimblehq.data.model.User
import co.nimblehq.usecase.user.LoadCurrentUserInfoSingleUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.robolectric.util.ReflectionHelpers

class UserViewModelTest {

    private lateinit var mockLoadCurrentUserInfoSingleUseCase: LoadCurrentUserInfoSingleUseCase

    private lateinit var userViewModel: UserViewModel

    @Before
    fun setUp() {
        mockLoadCurrentUserInfoSingleUseCase = mock()
    }

    @Test
    fun `When loading current user info failed, it triggers a GetCurrentUserInfoError`() {
        // Arrange
        whenever(
            mockLoadCurrentUserInfoSingleUseCase.execute(any())
        ) doReturn Single.error(UserError.LoadCurrentUserInfoError(null))

        // Act
        userViewModel = UserViewModel(mockLoadCurrentUserInfoSingleUseCase)
        val errorObserver = userViewModel
            .output
            .error
            .test()
        ReflectionHelpers.callInstanceMethod<UserViewModel>(userViewModel, "loadCurrentUserInfo")

        // Assert
        errorObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it is UserError.LoadCurrentUserInfoError }
    }

    @Test
    fun `When loading current user info successfully, it broadcast the User object to all places that are listening to it`() {
        // Arrange
        val user = User(id = "id", email = "minh@nimblehq.co", avatarUrl = "avatar")
        whenever(
            mockLoadCurrentUserInfoSingleUseCase.execute(any())
        ) doReturn Single.just(user)

        // Act
        userViewModel = UserViewModel(mockLoadCurrentUserInfoSingleUseCase)
        val userObserver = userViewModel
            .output
            .user
            .test()

        // Assert
        userObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue {
                it.email == user.email && user.avatarUrl == it.avatarUrl
            }
    }
}

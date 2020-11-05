package co.nimblehq.ui.screen.main

import co.nimblehq.usecase.user.LoadCurrentUserInfoSingleUseCase
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@Suppress("IllegalIdentifier")
class MainViewModelTest {

    private lateinit var mockLoadCurrentUserInfoSingleUseCase: LoadCurrentUserInfoSingleUseCase

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        mockLoadCurrentUserInfoSingleUseCase = mock()
        mainViewModel = MainViewModel(
            mockLoadCurrentUserInfoSingleUseCase
        )
    }

    @Test
    fun `First test description for MainViewModel`() {
        // TODO: Update to the actual test
        Assert.assertEquals(4, 2 + 2)
    }
}

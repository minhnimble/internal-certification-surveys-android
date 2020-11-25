package co.nimblehq.ui.screen.main

import org.junit.Assert
import org.junit.Before
import org.junit.Test

@Suppress("IllegalIdentifier")
class MainViewModelTest {

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        mainViewModel = MainViewModel()
    }

    @Test
    fun `First test description for MainViewModel`() {
        // TODO: Update to the actual test
        Assert.assertEquals(4, 2 + 2)
    }
}

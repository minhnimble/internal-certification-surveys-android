package co.nimblehq.extension

import android.content.Context
import android.view.View
import androidx.test.core.app.ApplicationProvider
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@Suppress("IllegalIdentifier")
@RunWith(RobolectricTestRunner::class)
class ViewExtensionKtTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun `using startFadeInAnimation should initially set alpha to 0`() {

        // Arrange
        val view = View(context)

        // Act
        view.startFadeInAnimation()

        // Assert
        assertEquals("alpha should be initially equal 0f", 0f, view.alpha)
    }

    @Test
    fun `using startFadeInAnimation should trigger action and update alpha to 1 when completed`() {

        // Arrange
        val view = View(context)
        var result = 5

        // Act
        view.startFadeInAnimation {
            result = 6

            // Assert
            assertEquals("completion block should exec when completed", 6, result)
            assertEquals("alpha should be updated to 1f when completed", 1f, view.alpha)
        }
    }

    @Test
    fun `using startFadeInAnimation with shouldAnimate = false should set view's alpha to 1f in the next runloop`() {

        // Arrange
        val view = View(context)

        // Act
        view.startFadeInAnimation(0)

        // Assert
        view.post {
            assertEquals("alpha should be equal 1f in the next runloop", 1f, view.alpha)
        }
    }
}

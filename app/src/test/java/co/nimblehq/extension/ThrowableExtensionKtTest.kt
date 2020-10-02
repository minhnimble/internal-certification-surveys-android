package co.nimblehq.extension

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import co.nimblehq.R
import co.nimblehq.data.error.AppError
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@Suppress("IllegalIdentifier")
@RunWith(RobolectricTestRunner::class)
class ThrowableExtensionKtTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun `using userReadableMessage with AppError object should return the message provided to AppError`() {

        // Arrange
        val errorMessage = "There is an error"
        var appError = AppError(null, errorMessage, null)

        // Act
        val userReadableMessage = appError.userReadableMessage(context)

        // Assert
        assertEquals("userReadableMessage val should be 'There is an error'", errorMessage, userReadableMessage)
    }

    @Test
    fun `using userReadableMessage with AppError object should return context's message when there is no provided message`() {

        // Arrange
        val errorMessage = context.getString(R.string.test_error)
        var appError = AppError(null, null, R.string.test_error)

        // Act
        val userReadableMessage = appError.userReadableMessage(context)

        // Assert
        assertEquals("userReadableMessage val should be the value of 'R.string.error_test'", errorMessage, userReadableMessage)
    }

    @Test
    fun `using userReadableMessage with AppError object should return default message when there is no context and no provided message`() {

        // Arrange
        val errorMessage = context.getString(R.string.generic_error)
        var appError = AppError(null, null, null)

        // Act
        val userReadableMessage = appError.userReadableMessage(context)

        // Assert
        assertEquals("userReadableMessage val should be the default value - 'R.string.generic_error'", errorMessage, userReadableMessage)
    }

    @Test
    fun `using userReadableMessage with Throwable object should return default message`() {

        // Arrange
        val errorMessage = context.getString(R.string.generic_error)
        var throwable = Throwable()

        // Act
        val userReadableMessage = throwable.userReadableMessage(context)

        // Assert
        assertEquals("userReadableMessage val should be the default value - 'R.string.generic_error'", errorMessage, userReadableMessage)
    }
}

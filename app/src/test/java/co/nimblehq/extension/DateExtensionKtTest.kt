package co.nimblehq.extension

import co.nimblehq.data.lib.common.DATE_FORMAT_SHORT_DISPLAY
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

@Suppress("IllegalIdentifier")
class DateExtensionKtTest {

    @Test
    fun `using toDisplayFormat should return an empty string if provided format string is invalid`() {
        // Arrange
        val invalidDateFormat = "invalid date format"
        val date = Date()

        // Act
        val result = date.toDisplayFormat(invalidDateFormat)

        // Assert
        assertEquals("toDisplayFormat conversion should return an empty string", "", result)
    }

    @Test
    fun `using toDisplayFormat should return a date string with provided format if provided format string is valid`() {
        // Arrange
        val timestamp = 1602288000
        val date = Date(timestamp * 1000L) // Sat, 10 Oct 2020 00:00:00 +0000

        // Act
        val result = date.toDisplayFormat(DATE_FORMAT_SHORT_DISPLAY) // provided format "EEEE, MMMM dd"

        // Assert
        assertEquals("toDisplayFormat conversion should return a formatted date string", "Saturday, October 10", result)
    }
}

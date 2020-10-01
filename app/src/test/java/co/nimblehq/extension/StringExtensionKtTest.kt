package co.nimblehq.extension

import org.junit.Test
import org.junit.Assert.*

@Suppress("IllegalIdentifier")
class StringExtensionKtTest {

    @Test
    fun `using isEmail should return true if provided string is a valid email`() {
        // Arrange
        var validEmail = "abc@nimblehq.co"

        // Act
        val result = validEmail.isEmail()

        // Assert
        assertTrue("isEmail check should be true", result)
    }

    @Test
    fun `using isEmail should return false if provided string is a valid email`() {
        // Arrange
        var invalidEmail = "abcd"

        // Act
        val result = invalidEmail.isEmail()

        // Assert
        assertFalse("isEmail check should be false", result)
    }
}

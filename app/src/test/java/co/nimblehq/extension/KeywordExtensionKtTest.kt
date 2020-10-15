package co.nimblehq.extension

import org.junit.Test
import org.junit.Assert.*

@Suppress("IllegalIdentifier")
class KeywordExtensionKtTest {

    @Test
    fun `using unless should not execute if the condition is true`() {

        // Arrange
        var result = 3
        val condition: Int = -1

        // Act
        unless(condition > -1) { result = 4 }

        // Assert
        assertEquals("block should exec", 4, result)
    }

    @Test
    fun `using unless should execute if the condition is false`() {

        // Arrange
        var result = 3
        val condition: Int = -1

        // Act
        unless(condition == -1) { result = 5 }

        // Assert
        assertEquals("block should NOT exec", 3, result)
    }
}

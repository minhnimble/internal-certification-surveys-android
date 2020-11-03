package co.nimblehq.extension

import android.content.Context
import android.widget.ArrayAdapter
import androidx.test.core.app.ApplicationProvider
import co.nimblehq.R
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@Suppress("IllegalIdentifier")
@RunWith(RobolectricTestRunner::class)
class ArrayAdapterExtensionTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun `using refreshWithData should update the array adapter with new data`() {
        // Arrange
        val adapter = ArrayAdapter<String>(context, R.layout.item_survey_questions_dropdown_answer_selected, arrayListOf())
        val item1 = "Item 1"
        val item2 = "Item 2"

        // Act
        adapter.refreshWithData(listOf(item1, item2))

        // Assert
        Assert.assertEquals("refreshWithData should update adapter's data with new contents, item at index 0 is `Item 1`", item1, adapter.getItem(0))
        Assert.assertEquals("refreshWithData should update adapter's data with new contents, item at index 1 is `Item 2`", item2, adapter.getItem(1))
    }
}

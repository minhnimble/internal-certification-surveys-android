package co.nimblehq.data.storage

import android.content.SharedPreferences
import androidx.core.content.edit
import io.reactivex.Completable
import javax.inject.Inject

interface AppPreferences {

    var surveysCurrentPage: Int?

    var surveysTotalPages: Int?

    // TODO: Will use this function when implementing logout feature
    fun clearAll() = Completable.fromAction {
        surveysCurrentPage = null
        surveysTotalPages = null
    }
}

class AppPreferencesImpl @Inject constructor(
    private val preferences: SharedPreferences
) : AppPreferences {

    override var surveysCurrentPage: Int?
        get() = getInt(KEY_SURVEYS_CURRENT_PAGE)
        set(page) = setOrRemove(KEY_SURVEYS_CURRENT_PAGE, page)

    override var surveysTotalPages: Int?
        get() = getInt(KEY_SURVEYS_TOTAL_PAGES)
        set(pages) = setOrRemove(KEY_SURVEYS_TOTAL_PAGES, pages)

    private fun getString(key: String): String? {
        return preferences.getString(key, null)
    }

    private fun getInt(key: String): Int? {
        if (!preferences.contains(key)) {
            return null
        }
        return preferences.getInt(key, 0)
    }

    private fun getBoolean(key: String): Boolean? {
        if (!preferences.contains(key)) {
            return null
        }
        return preferences.getBoolean(key, false)
    }

    private fun getLong(key: String): Long? {
        if (!preferences.contains(key)) {
            return null
        }
        return preferences.getLong(key, 0)
    }

    private fun setOrRemove(key: String, value: Any?) {
        preferences.edit {
            when (value) {
                null -> remove(key)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is String -> putString(key, value)
                is Long -> putLong(key, value)
                else -> throw IllegalArgumentException("Unsupported Type")
            }
        }
    }
}

private const val KEY_SURVEYS_CURRENT_PAGE = "surveys_current_page"
private const val KEY_SURVEYS_TOTAL_PAGES = "surveys_total_pages"

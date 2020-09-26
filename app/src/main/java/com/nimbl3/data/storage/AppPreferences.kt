package com.nimbl3.data.storage

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

interface AppPreferences {
    // TODO: Add new keys here
}

class AppPreferencesImpl @Inject constructor(
    private val preferences: SharedPreferences
) : AppPreferences {

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
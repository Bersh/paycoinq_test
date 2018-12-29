package example.com.archtest

import android.content.Context
import android.preference.PreferenceManager

class PreferencesManager {
    companion object {
        val KEY_LAST_PAGE = "last_loaded_page"
        fun saveLoadedPage(context: Context, pageNumber: Int) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            preferences.edit().putInt(KEY_LAST_PAGE, pageNumber).apply()
        }

        fun getLoadedPage(context: Context): Int {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(KEY_LAST_PAGE, 0)
        }
    }
}
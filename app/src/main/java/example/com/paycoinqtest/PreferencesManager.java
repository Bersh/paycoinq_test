package example.com.paycoinqtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

public abstract class PreferencesManager {
	private static String KEY_LAST_PAGE = "last_loaded_page";

	public static void saveLoadedPage(@NonNull Context context, @NonNull int pageNumber) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		preferences.edit().putInt(KEY_LAST_PAGE, pageNumber).apply();
	}

	@NonNull
	public static int getLoadedPage(@NonNull Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getInt(KEY_LAST_PAGE, 0);
	}
}

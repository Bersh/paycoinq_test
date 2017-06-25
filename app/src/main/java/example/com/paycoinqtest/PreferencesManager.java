package example.com.paycoinqtest;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class PreferencesManager {
/*	private static String KEY_CITY = "city";

	public static void saveCities(@NonNull Context context, @NonNull List<String> cities) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		preferences.edit().putString(KEY_CITY, listToString(cities)).apply();
	}

	@NonNull
	public static List<String> getCities(@NonNull Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return stringToList(preferences.getString(KEY_CITY, ""));
	}

	public static void clearCities(@NonNull Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		preferences.edit().remove(KEY_CITY).apply();
	}

	@NonNull
	private static String listToString(@NonNull List<String> list) {
		String tmp = list.toString();
		return tmp.substring(1, tmp.length() - 1).replace(", ", ",");
	}

	@NonNull
	private static List<String> stringToList(String str) {
		if (TextUtils.isEmpty(str)) {
			return new ArrayList<>();
		} else {
			return new ArrayList<>(Arrays.asList(str.split(",")));
		}
	}*/
}

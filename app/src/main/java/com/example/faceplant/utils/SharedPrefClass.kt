package com.example.faceplant.utils
import android.app.Activity

class SharedPrefsClass {
    // Function to store user data
    fun setSharedPreference(activity: Activity, prefName: String?, key: String?, value: String?) {
        val sharedPrefs = activity.getSharedPreferences(prefName, 0)
        val editor = sharedPrefs.edit()

        editor.putString(key, value)
        editor.apply()
    }

    // Function to get store user data
    fun getSharedPreference(activity: Activity, prefName: String?, key: String?, defaultValue: String?
    ): String? {
        val sharedPrefs = activity.getSharedPreferences(prefName, 0)
        return sharedPrefs.getString(key, defaultValue)
    }

    // Function to clear stored user data after sign out
    fun clearSharedPreference(activity: Activity, prefName: String?, key: String?) {
        val sharedPrefs = activity.getSharedPreferences(prefName, 0)
        val editor = sharedPrefs.edit()

        editor.clear()
        editor.apply()
    }
}
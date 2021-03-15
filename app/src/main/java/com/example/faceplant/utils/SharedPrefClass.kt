package com.example.faceplant.utils
import android.app.Activity


class SharedPrefsClass {
    // Function to save data
    fun setSharedPreference(activity: Activity, prefName: String?, key: String?, value: String?) {
        val sharedPrefs = activity.getSharedPreferences(prefName, 0)
        val editor = sharedPrefs.edit()

        editor.putString(key, value)
        editor.apply()
    }

    fun getSharedPreference(activity: Activity, prefName: String?, key: String?, defaultValue: String?
    ): String? {
        val sharedPrefs = activity.getSharedPreferences(prefName, 0)
        return sharedPrefs.getString(key, defaultValue)
    }

    fun clearSharedPreference(activity: Activity, prefName: String?, key: String?) {
        val sharedPrefs = activity.getSharedPreferences(prefName, 0)
        val editor = sharedPrefs.edit()

        editor.clear()
        editor.apply()
    }
}

/*
               val sharedPreferences = activity.getSharedPreferences(
                   Constants.FACEPLANT_PREFERENCES,
                   Context.MODE_PRIVATE
               )

               val editor: SharedPreferences.Editor = sharedPreferences.edit()
               // Saving username inside the sharedPreferences key: UsernamePrefKey
               editor.putString(Constants.USERNAME_PREF_KEY, user?.username)
               editor.apply()

               when(activity){
                   is SignInActivity -> {
                       if (user != null) {
                           activity.userSignInSuccess(user)
                       }
                   }
                   is SplashActivity -> {
                       if (user != null) {
                           activity.userAlreadySignedIn(user)
                       }
                   }
               }
               */
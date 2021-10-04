package com.antsfamily.restafinder.data.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class SharedPrefs @Inject constructor(context: Context) {

    private val prefsName = "com.antsfamily.restafinder.data.local.shared_prefs"

    private val preferences: SharedPreferences =
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    var favouriteRestaurantsIds: Set<String>
        get() {
            val restaurants =
                preferences.getStringSet(KEY_FAVOURITE_RESTAURANT_IDS, emptySet<String>())
            return restaurants ?: emptySet()
        }
        set(value) = preferences.edit().putStringSet(KEY_FAVOURITE_RESTAURANT_IDS, value).apply()

    companion object {
        private const val KEY_FAVOURITE_RESTAURANT_IDS = "key_favourite_restaurant_ids"
    }
}

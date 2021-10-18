package com.example.seminarsample.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.seminarsample.BuildConfig
import javax.inject.Inject

open class Pref @Inject constructor(
    private val context: Context
) {

    fun setStringValue(key: String, value: String?) {
        val prefs: SharedPreferences = context.getSharedPreferences(getKey(), Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun setBoolValue(key: String, value: Boolean) {
        val prefs: SharedPreferences = context.getSharedPreferences(getKey(), Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun setIntValue(key: String, value: Int) {
        val prefs: SharedPreferences = context.getSharedPreferences(getKey(), Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getStringValue(key: String): String? {
        val prefs: SharedPreferences = context.getSharedPreferences(getKey(), Context.MODE_PRIVATE)
        return prefs.getString(key, "")
    }

    fun getBoolVal(key: String): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(getKey(), Context.MODE_PRIVATE)
        return prefs.getBoolean(key, false)
    }

    fun getIntValue(key: String): Int {
        val prefs: SharedPreferences = context.getSharedPreferences(getKey(), Context.MODE_PRIVATE)
        return prefs.getInt(key, 0)
    }

    private fun getKey(): String {
        return "${BuildConfig.APPLICATION_ID}.PREF"
    }
}
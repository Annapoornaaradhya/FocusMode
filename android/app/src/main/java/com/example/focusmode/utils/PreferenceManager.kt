package com.example.focusmode.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("FocusModePrefs", Context.MODE_PRIVATE)

    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean("dark_mode", enabled).apply()
    }

    fun isDarkMode(): Boolean = prefs.getBoolean("dark_mode", true)

    fun setSoundEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("sound", enabled).apply()
    }

    fun isSoundEnabled(): Boolean = prefs.getBoolean("sound", true)

    fun setVibrationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("vibration", enabled).apply()
    }

    fun isVibrationEnabled(): Boolean = prefs.getBoolean("vibration", true)

    fun setFocusDuration(minutes: Int) {
        prefs.edit().putInt("focus_duration", minutes).apply()
    }

    fun getFocusDuration(): Int = prefs.getInt("focus_duration", 25)

    fun setShortBreakDuration(minutes: Int) {
        prefs.edit().putInt("short_break", minutes).apply()
    }

    fun getShortBreakDuration(): Int = prefs.getInt("short_break", 5)

    fun setAppBlocked(packageName: String, blocked: Boolean) {
        prefs.edit().putBoolean("block_$packageName", blocked).apply()
    }

    fun isAppBlocked(packageName: String): Boolean = prefs.getBoolean("block_$packageName", false)

    fun incrementStreak() {
        val current = prefs.getInt("streak", 0)
        prefs.edit().putInt("streak", current + 1).apply()
    }

    fun getStreak(): Int = prefs.getInt("streak", 0)
}

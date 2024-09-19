package br.com.fiap.challengerlocalweb

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val USER_ID = "userId"
        private const val USER_TOKEN = "token"
        private const val USER_NAME = "name"
        private const val USER_EMAIL = "email"
        private const val USER_THEME = "user_theme"
        private const val USER_COLOR_SCHEME = "user_color_scheme"

        @Volatile
        private var instance: SessionManager? = null

        fun initialize(context: Context) {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = SessionManager(context)
                    }
                }
            }
        }

        fun getInstance(): SessionManager {
            return instance ?: throw IllegalStateException("SessionManager must be initialized first")
        }
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveUserId(userId: String) {
        val editor = prefs.edit()
        editor.putString(USER_ID, userId)
        editor.apply()
    }

    fun fetchUserId(): String? {
        return prefs.getString(USER_ID, null)
    }

    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    fun saveUserName(name: String) {
        val editor = prefs.edit()
        editor.putString(USER_NAME, name)
        editor.apply()
    }

    fun fetchUserName(): String? {
        return prefs.getString(USER_NAME, null)
    }

    fun saveUserEmail(email: String) {
        val editor = prefs.edit()
        editor.putString(USER_EMAIL, email)
        editor.apply()
    }

    fun fetchUserEmail(): String? {
        return prefs.getString(USER_EMAIL, null)
    }

    fun saveUserTheme(theme: String) {
        val editor = prefs.edit()
        editor.putString(USER_THEME, theme)
        editor.apply()
    }

    fun fetchUserTheme(): String? {
        return prefs.getString(USER_THEME, null)
    }

    fun saveUserColorScheme(colorScheme: String) {
        val editor = prefs.edit()
        editor.putString(USER_COLOR_SCHEME, colorScheme)
        editor.apply()
    }

    fun fetchUserColorScheme(): String? {
        return prefs.getString(USER_COLOR_SCHEME, null)
    }
}

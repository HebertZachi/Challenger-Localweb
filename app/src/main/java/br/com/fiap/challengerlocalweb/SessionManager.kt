package br.com.fiap.challengerlocalweb

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    // Definindo SharedPreferences para armazenar as preferências do usuário
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

        // Método para inicializar a instância do SessionManager
        fun initialize(context: Context) {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = SessionManager(context)
                    }
                }
            }
        }

        // Método para retornar a instância do SessionManager
        fun getInstance(): SessionManager {
            return instance ?: throw IllegalStateException("SessionManager must be initialized first")
        }
    }

    // Método para salvar o token de autenticação
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    // Método para buscar o token de autenticação
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    // Método para salvar o ID do usuário
    fun saveUserId(userId: String) {
        val editor = prefs.edit()
        editor.putString(USER_ID, userId)
        editor.apply()
    }

    // Método para buscar o ID do usuário
    fun fetchUserId(): String? {
        return prefs.getString(USER_ID, null)
    }

    // Método para limpar a sessão do usuário
    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    // Método para salvar o nome do usuário
    fun saveUserName(name: String) {
        val editor = prefs.edit()
        editor.putString(USER_NAME, name)
        editor.apply()
    }

    // Método para buscar o nome do usuário
    fun fetchUserName(): String? {
        return prefs.getString(USER_NAME, null)
    }

    // Método para salvar o e-mail do usuário
    fun saveUserEmail(email: String) {
        val editor = prefs.edit()
        editor.putString(USER_EMAIL, email)
        editor.apply()
    }

    // Método para buscar o e-mail do usuário
    fun fetchUserEmail(): String? {
        return prefs.getString(USER_EMAIL, null)
    }

    // Método para salvar o tema do usuário (DARK ou LIGHT)
    fun saveUserTheme(theme: String) {
        val editor = prefs.edit()
        editor.putString(USER_THEME, theme)
        editor.apply()
    }

    // Método para buscar o tema do usuário. Não deve retornar valor padrão "LIGHT" se o usuário já tiver uma preferência.
    fun fetchUserTheme(): String? {
        return prefs.getString(USER_THEME, null)  // Retorna null se não estiver salvo
    }

    // Método para salvar o esquema de cores do usuário
    fun saveUserColorScheme(colorScheme: String) {
        val editor = prefs.edit()
        editor.putString(USER_COLOR_SCHEME, colorScheme)
        editor.apply()
    }

    // Método para buscar o esquema de cores do usuário. Não deve retornar valor padrão "#000000" se o usuário já tiver uma preferência.
    fun fetchUserColorScheme(): String? {
        return prefs.getString(USER_COLOR_SCHEME, null)  // Retorna null se não estiver salvo
    }
}

package br.com.fiap.challengerlocalweb.theme

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.fiap.challengerlocalweb.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class UserThemeManager(private val context: Context) {

    private val sessionManager = SessionManager.getInstance()

    // Estado de cores globais
    private val _isDarkMode = MutableLiveData(false)
    val isDarkMode: LiveData<Boolean> get() = _isDarkMode

    private val _selectedTextColor = mutableStateOf(Color.Black)
    val selectedTextColor get() = _selectedTextColor

    private val _backgroundColor = mutableStateOf(Color.White)
    val backgroundColor get() = _backgroundColor

    // Função para carregar as preferências do usuário
    suspend fun loadUserPreferences(onError: (String) -> Unit = {}) {
        val userId = sessionManager.fetchUserId() ?: run {
            onError("Erro: ID do usuário não encontrado.")
            return
        }
        val token = sessionManager.fetchAuthToken() ?: run {
            onError("Erro: Token de autenticação não encontrado.")
            return
        }

        val url = "http://10.0.2.2:8080/api/user/preferences?id=$userId"
        val client = OkHttpClient()

        // Requisição para buscar as preferências do usuário
        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("Authorization", "Bearer $token")
            .build()

        withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val jsonString = response.body?.string() ?: return@withContext
                    val json = JSONObject(jsonString)

                    val theme = json.getString("theme")
                    val colorScheme = json.getString("colorScheme")

                    // Aplicar o tema e a cor
                    withContext(Dispatchers.Main) {
                        _isDarkMode.value = theme == "DARK"
                        _selectedTextColor.value = Color(android.graphics.Color.parseColor(colorScheme))
                        _backgroundColor.value = if (_isDarkMode.value == true) Color.Black else Color.White
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError("Erro ao carregar preferências: ${response.message}")
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onError("Erro de conexão: ${e.message}")
                }
            }
        }
    }

    // Função para alterar o tema dinamicamente
    fun toggleDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
        _backgroundColor.value = if (enabled) Color.Black else Color.White
        sessionManager.saveUserTheme(if (enabled) "DARK" else "LIGHT")
    }

    // Função para alterar a cor de texto dinamicamente
    fun updateTextColor(newColor: Color) {
        _selectedTextColor.value = newColor
        sessionManager.saveUserColorScheme("#${Integer.toHexString(newColor.toArgb())}")
    }

    // Função para salvar as preferências (enviar para o backend)
    suspend fun saveUserPreferences(onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        val userId = sessionManager.fetchUserId() ?: run {
            onError("Erro: ID do usuário não encontrado.")
            return
        }
        val token = sessionManager.fetchAuthToken() ?: run {
            onError("Erro: Token de autenticação não encontrado.")
            return
        }

        val url = "http://10.0.2.2:8080/api/user/preferences"
        val client = OkHttpClient()

        val json = JSONObject().apply {
            put("theme", if (_isDarkMode.value == true) "DARK" else "LIGHT")
            put("colorScheme", String.format("#%06X", 0xFFFFFF and _selectedTextColor.value.toArgb()))
            put("user", userId)
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("Authorization", "Bearer $token")
            .build()

        withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        onSuccess()
                    }
                } else {
                    val errorMessage = response.body?.string() ?: "Erro desconhecido"
                    withContext(Dispatchers.Main) {
                        onError("Erro ao salvar preferências: ${response.message}. Detalhes: $errorMessage")
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onError("Erro de conexão: ${e.message}")
                }
            }
        }
    }
}

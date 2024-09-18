package br.com.fiap.challengerlocalweb.pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.challengerlocalweb.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun userPrefs(
    navController: NavController,
    context: Context
) {
    // Inicializa o SessionManager para manipular preferências
    val sessionManager = SessionManager.getInstance()

    // Estado para armazenar o tema e cor de texto
    var darkModeEnabled by remember { mutableStateOf(false) }
    var selectedTextColor by remember { mutableStateOf(Color.Black) }
    var backgroundColor by remember { mutableStateOf(Color.White) }  // Cor de fundo padrão para Light Mode

    val coroutineScope = rememberCoroutineScope()

    // Carregar as preferências do SessionManager ao abrir a tela
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            // Busca as preferências do usuário no SessionManager
            val savedTheme = sessionManager.fetchUserTheme() ?: "LIGHT"
            val savedColorScheme = sessionManager.fetchUserColorScheme() ?: "#000000"
            val token = sessionManager.fetchAuthToken() ?: ""  // Obtém o token do SessionManager

            darkModeEnabled = savedTheme == "DARK"
            selectedTextColor = Color(android.graphics.Color.parseColor(savedColorScheme))
            backgroundColor = if (darkModeEnabled) Color.Black else Color.White
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Email, contentDescription = "Emails Recebidos") },
                    label = { Text("Recebidos") },
                    selected = false,
                    onClick = { navController.navigate("inbox") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = false,
                    onClick = { navController.navigate("userProfile") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Preferências") },
                    label = { Text("Preferências") },
                    selected = true,
                    onClick = { /* Tela atual */ }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(backgroundColor)  // Aplica a cor de fundo conforme o estado do tema
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Preferências do Usuário",
                    fontSize = 24.sp,
                    color = selectedTextColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Alternar Modo Escuro
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Modo Escuro", fontSize = 18.sp, color = selectedTextColor)
                    Switch(
                        checked = darkModeEnabled,
                        onCheckedChange = {
                            darkModeEnabled = it
                            backgroundColor = if (darkModeEnabled) Color.Black else Color.White

                            // Salvar as preferências de tema ao alternar o switch
                            coroutineScope.launch {
                                sessionManager.saveUserTheme(if (darkModeEnabled) "DARK" else "LIGHT")
                                saveUserPreferences(context, sessionManager.fetchUserId()!!, if (darkModeEnabled) "DARK" else "LIGHT", selectedTextColor, sessionManager.fetchAuthToken()!!)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.secondary,
                            uncheckedThumbColor = Color.Gray
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Texto de instrução para seleção de cor
                Text(
                    text = "Escolha a cor do texto",
                    fontSize = 18.sp,
                    color = selectedTextColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Opções de cores predefinidas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ColorOption(color = Color.Black, onColorSelected = { newColor ->
                        selectedTextColor = newColor
                        coroutineScope.launch {
                            sessionManager.saveUserColorScheme("#000000")  // Salva no SessionManager
                            saveUserPreferences(context, sessionManager.fetchUserId()!!, if (darkModeEnabled) "DARK" else "LIGHT", selectedTextColor, sessionManager.fetchAuthToken()!!)
                        }
                    })
                    ColorOption(color = Color.Red, onColorSelected = { newColor ->
                        selectedTextColor = newColor
                        coroutineScope.launch {
                            sessionManager.saveUserColorScheme("#FF0000")  // Salva no SessionManager
                            saveUserPreferences(context, sessionManager.fetchUserId()!!, if (darkModeEnabled) "DARK" else "LIGHT", selectedTextColor, sessionManager.fetchAuthToken()!!)
                        }
                    })
                    ColorOption(color = Color.Blue, onColorSelected = { newColor ->
                        selectedTextColor = newColor
                        coroutineScope.launch {
                            sessionManager.saveUserColorScheme("#0000FF")  // Salva no SessionManager
                            saveUserPreferences(context, sessionManager.fetchUserId()!!, if (darkModeEnabled) "DARK" else "LIGHT", selectedTextColor, sessionManager.fetchAuthToken()!!)
                        }
                    })
                    ColorOption(color = Color.Green, onColorSelected = { newColor ->
                        selectedTextColor = newColor
                        coroutineScope.launch {
                            sessionManager.saveUserColorScheme("#008000")  // Salva no SessionManager
                            saveUserPreferences(context, sessionManager.fetchUserId()!!, if (darkModeEnabled) "DARK" else "LIGHT", selectedTextColor, sessionManager.fetchAuthToken()!!)
                        }
                    })
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botão para voltar ao perfil
                Button(
                    onClick = { navController.navigate("userProfile") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                ) {
                    Text(text = "Voltar ao Perfil")
                }
            }
        }
    }
}

suspend fun saveUserPreferences(
    context: Context,
    userId: String,
    theme: String,
    textColor: Color,
    token: String,
    onResult: (Boolean, String?) -> Unit = { _, _ -> }  // Callback opcional para mostrar sucesso ou erro
) {
    val url = "http://192.168.0.120:8080/api/user/preferences"
    val client = OkHttpClient()

    // Converta a cor para uma string hexadecimal
    val colorHex = String.format("#%06X", 0xFFFFFF and textColor.toArgb())

    // Crie o objeto JSON com as preferências
    val json = JSONObject().apply {
        put("theme", theme)
        put("colorScheme", colorHex)
        put("user", userId)
    }

    // Crie o corpo da requisição com os dados JSON
    val requestBody: RequestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
    val request = Request.Builder()
        .url(url)
        .post(requestBody)  // Mudando para POST para criar ou atualizar as preferências
        .addHeader("Authorization", "Bearer $token")
        .build()

    // Executa a requisição em um contexto de IO
    withContext(Dispatchers.IO) {
        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    onResult(true, null)  // Chama o callback de sucesso
                }
            } else {
                val errorMessage = response.body?.string() ?: "Erro desconhecido"
                withContext(Dispatchers.Main) {
                    onResult(false, "Erro: ${response.message}. Detalhes: $errorMessage")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                onResult(false, e.message)
            }
        }
    }
}

@Composable
fun ColorOption(color: Color, onColorSelected: (Color) -> Unit) {
    Button(
        onClick = { onColorSelected(color) },
        modifier = Modifier
            .size(40.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color) // Definindo a cor correta do botão
    ) {
        // O conteúdo do botão pode ficar vazio, já que é apenas um seletor de cor
    }
}

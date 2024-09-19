package br.com.fiap.challengerlocalweb.pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.challengerlocalweb.SessionManager
import br.com.fiap.challengerlocalweb.theme.UserThemeManager
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
fun changePassword(navController: NavController, userThemeManager: UserThemeManager) {
    var oldPassword by remember { mutableStateOf(TextFieldValue("")) }
    var newPassword by remember { mutableStateOf(TextFieldValue("")) }
    var confirmPassword by remember { mutableStateOf(TextFieldValue("")) }

    var oldPasswordError by remember { mutableStateOf(false) }
    var newPasswordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }
    var passwordMismatchError by remember { mutableStateOf(false) }
    var newPasswordLengthError by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // Aplicando as cores do tema atual do usuário
    val backgroundColor = userThemeManager.backgroundColor.value
    val textColor = userThemeManager.selectedTextColor.value

    fun validateFields(): Boolean {
        oldPasswordError = oldPassword.text.isEmpty()
        newPasswordError = newPassword.text.isEmpty()
        confirmPasswordError = confirmPassword.text.isEmpty()
        passwordMismatchError = newPassword.text != confirmPassword.text
        newPasswordLengthError = newPassword.text.length < 8

        return !oldPasswordError && !newPasswordError && !confirmPasswordError && !passwordMismatchError && !newPasswordLengthError
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Email, contentDescription = "Emails Recebidos") },
                    label = { Text("Recebidos", color = textColor) },
                    selected = false,
                    onClick = { navController.navigate("inbox") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Send, contentDescription = "Emails Enviados") },
                    label = { Text("Enviados", color = textColor) },
                    selected = false,
                    onClick = { navController.navigate("sentItems") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.DateRange, contentDescription = "Calendário") },
                    label = { Text("Calendário", color = textColor) },
                    selected = false,
                    onClick = { navController.navigate("calendar") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil", color = textColor) },
                    selected = true,
                    onClick = { navController.navigate("userProfile") }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(backgroundColor)
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Alterar Senha",
                    fontSize = 24.sp,
                    color = textColor,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Senha Antiga", color = textColor) },
                    textStyle = TextStyle(color = textColor),
                    visualTransformation = PasswordVisualTransformation(),
                    isError = oldPasswordError,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = textColor,
                        unfocusedBorderColor = textColor.copy(alpha = 0.5f),
                        cursorColor = textColor
                    )
                )
                if (oldPasswordError) {
                    Text("Senha antiga não pode estar vazia", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nova Senha", color = textColor) },
                    textStyle = TextStyle(color = textColor),
                    visualTransformation = PasswordVisualTransformation(),
                    isError = newPasswordError || newPasswordLengthError,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = textColor,
                        unfocusedBorderColor = textColor.copy(alpha = 0.5f),
                        cursorColor = textColor
                    )
                )
                if (newPasswordError) {
                    Text("Nova senha não pode estar vazia", color = Color.Red, fontSize = 12.sp)
                } else if (newPasswordLengthError) {
                    Text("A nova senha deve ter pelo menos 8 caracteres", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Confirmar Nova Senha", color = textColor) },
                    textStyle = TextStyle(color = textColor),
                    visualTransformation = PasswordVisualTransformation(),
                    isError = confirmPasswordError || passwordMismatchError,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = textColor,
                        unfocusedBorderColor = textColor.copy(alpha = 0.5f),
                        cursorColor = textColor
                    )
                )
                if (confirmPasswordError) {
                    Text("Confirmação de senha não pode estar vazia", color = Color.Red, fontSize = 12.sp)
                } else if (passwordMismatchError) {
                    Text("As senhas não coincidem", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (validateFields()) {
                            coroutineScope.launch {
                                val sessionManager = SessionManager.getInstance()
                                val token = sessionManager.fetchAuthToken()
                                val userId = sessionManager.fetchUserId()
                                val email = sessionManager.fetchUserEmail()
                                val name = sessionManager.fetchUserName()

                                // Chamar função para trocar senha
                                updatePassword(
                                    context = context,
                                    userId = userId!!,
                                    name = name!!,
                                    email = email!!,
                                    currentPassword = oldPassword.text,
                                    newPassword = newPassword.text,
                                    token = token!!
                                ) { success, errorMessage ->
                                    if (success) {
                                        Toast.makeText(context, "Senha alterada com sucesso", Toast.LENGTH_SHORT).show()
                                        navController.navigate("userProfile")
                                    } else {
                                        Toast.makeText(context, "Erro: $errorMessage", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(context, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Alterar Senha", color = textColor)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("userProfile") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Cancelar", color = textColor)
                }
            }
        }
    }
}

suspend fun updatePassword(
    context: Context,
    userId: String,
    name: String,
    email: String,
    currentPassword: String,
    newPassword: String,
    token: String,
    onResult: (Boolean, String?) -> Unit
) {
    val url = "http://10.0.2.2:8080/api/user?id=$userId&email=$email"
    val client = OkHttpClient()

    val json = JSONObject().apply {
        put("name", name)
        put("email", email)
        put("currentPassword", currentPassword)
        put("newPassword", newPassword)
    }

    val requestBody: RequestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
    val request = Request.Builder()
        .url(url)
        .put(requestBody)
        .addHeader("Authorization", "Bearer $token")
        .build()

    withContext(Dispatchers.IO) {
        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    onResult(true, null)
                }
            } else if (response.code == 401) {
                val errorMessage = response.body?.string() ?: "Senha antiga incorreta"
                withContext(Dispatchers.Main) {
                    onResult(false, "Senha antiga incorreta")
                }
            } else {
                val errorMessage = response.body?.string() ?: "Erro desconhecido"
                withContext(Dispatchers.Main) {
                    onResult(false, "Erro: $errorMessage")
                }
            }
        } catch (e: IOException) {
            withContext(Dispatchers.Main) {
                onResult(false, e.message)
            }
        }
    }
}

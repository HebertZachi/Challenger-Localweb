package br.com.fiap.challengerlocalweb.pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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

@Composable
fun changeUserName(
    navController: NavController,
    context: Context
) {
    var newUserName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()


    val sessionManager = SessionManager.getInstance()
    val userId = sessionManager.fetchUserId() ?: ""
    val userEmail = sessionManager.fetchUserEmail() ?: ""
    val token = sessionManager.fetchAuthToken() ?: ""

    fun validateFields(): Boolean {
        return newUserName.isNotEmpty() && password.isNotEmpty()
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .background(Color(0xFF253645))
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
                    text = "Alterar Nome",
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = newUserName,
                    onValueChange = { newUserName = it },
                    label = { Text("Novo Nome", color = Color.White) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Senha", color = Color.White) },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = "Toggle Password Visibility", tint = Color.White)
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (validateFields()) {
                            coroutineScope.launch {
                                updateUserName(
                                    context = context,
                                    userId = userId,
                                    email = userEmail,
                                    newUserName = newUserName,
                                    password = password,
                                    token = token
                                ) { success, errorMessage ->
                                    if (success) {
                                        // Atualiza o nome no SessionManager
                                        sessionManager.saveUserName(newUserName)

                                        // Redireciona para o perfil
                                        navController.navigate("userProfile") {
                                            popUpTo("userProfile") { inclusive = true }
                                        }
                                        Toast.makeText(context, "Nome alterado com sucesso", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Erro: $errorMessage", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Salvar Nome")
                }
            }
        }
    }
}

suspend fun updateUserName(
    context: Context,
    userId: String,
    email: String,
    newUserName: String,
    password: String,
    token: String,
    onResult: (Boolean, String?) -> Unit
) {
    val url = "http://192.168.0.120:8080/api/user?id=$userId&email=$email"
    val client = OkHttpClient()

    val json = JSONObject().apply {
        put("name", newUserName)
        put("email", email)
        put("currentPassword", password)
        put("newPassword", password)
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

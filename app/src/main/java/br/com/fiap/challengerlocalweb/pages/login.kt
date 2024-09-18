package br.com.fiap.challengerlocalweb.pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.challengerlocalweb.R
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
fun login(navController: NavController, context: Context) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val logoSize = (screenWidth.coerceAtMost(screenHeight) * 0.5f)

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
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
                Image(
                    painter = painterResource(id = R.drawable.wave_logo),
                    contentDescription = "WaveMail Logo",
                    modifier = Modifier.size(logoSize)
                )

                Spacer(modifier = Modifier.height(5.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                append("Lo")
                            }
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                                append("gin")
                            }
                        },
                        fontSize = 50.sp,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail", color = MaterialTheme.colorScheme.onBackground) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onBackground),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Senha", color = MaterialTheme.colorScheme.onBackground) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = "Toggle Password Visibility", tint = MaterialTheme.colorScheme.onBackground)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onBackground),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            loginUser(context, email, password) { success, errorMessage ->
                                if (success) {
                                    coroutineScope.launch {
                                        fetchUserProfile(context) { profileSuccess, profileError ->
                                            if (profileSuccess) {
                                                navController.navigate("inbox")
                                            } else {
                                                Toast.makeText(context, "Erro ao buscar perfil: $profileError", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, "Erro ao fazer login: $errorMessage", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Login")
                }


                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = { navController.navigate("Signup") }
                ) {
                    Text("Não tem uma conta? Cadastre-se", color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    }
}

suspend fun loginUser(
    context: Context,
    email: String,
    password: String,
    onResult: (Boolean, String?) -> Unit
) {
    val url = "http://10.0.2.2:8080/auth/login"
    val client = OkHttpClient()

    val json = JSONObject().apply {
        put("email", email)
        put("password", password)
    }

    val requestBody: RequestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    withContext(Dispatchers.IO) {
        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && responseBody != null) {
                val responseJson = JSONObject(responseBody)

                val userId = responseJson.getString("userId")
                val token = responseJson.getString("token")

                SessionManager.getInstance().saveUserId(userId)
                SessionManager.getInstance().saveAuthToken(token)

                withContext(Dispatchers.Main) {
                    onResult(true, null)
                }
            } else {
                withContext(Dispatchers.Main) {
                    onResult(false, "Erro: ${response.message}")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                onResult(false, e.message)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                onResult(false, e.message)
            }
        }
    }
}

suspend fun fetchUserProfile(
    context: Context,
    onResult: (Boolean, String?) -> Unit
) {
    val userId = SessionManager.getInstance().fetchUserId()
    val token = SessionManager.getInstance().fetchAuthToken()

    if (userId.isNullOrEmpty() || token.isNullOrEmpty()) {
        onResult(false, "Token ou ID de usuário ausente")
        return
    }

    val url = "http://10.0.2.2:8080/api/user/find?id=$userId"
    val client = OkHttpClient()

    val request = Request.Builder()
        .url(url)
        .addHeader("Authorization", "Bearer $token")
        .build()

    withContext(Dispatchers.IO) {
        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseData = response.body?.string()
                if (!responseData.isNullOrEmpty()) {
                    val json = JSONObject(responseData)
                    val fetchedName = json.getString("name")
                    val fetchedEmail = json.getString("email")

                    SessionManager.getInstance().saveUserName(fetchedName)
                    SessionManager.getInstance().saveUserEmail(fetchedEmail)

                    withContext(Dispatchers.Main) {
                        onResult(true, null)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onResult(false, "Erro ao obter dados do usuário")
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    onResult(false, "Token inválido ou sessão expirada")
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

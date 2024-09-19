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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.challengerlocalweb.SessionManager
import br.com.fiap.challengerlocalweb.theme.UserThemeManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun userPrefs(
    navController: NavController,
    context: Context,
    userThemeManager: UserThemeManager
) {
    val sessionManager = SessionManager.getInstance()
    val coroutineScope = rememberCoroutineScope()
    val toastDuration = Toast.LENGTH_SHORT

    // Estado para armazenar o tema e cor de texto a partir do UserThemeManager
    var darkModeEnabled by remember { mutableStateOf(userThemeManager.isDarkMode.value ?: false) }
    var selectedTextColor by remember { mutableStateOf(userThemeManager.selectedTextColor.value ?: Color.Black) }
    var backgroundColor by remember { mutableStateOf(userThemeManager.backgroundColor.value ?: Color.White) }

    // Atualizar as preferências ao modificar o estado do tema ou cor de texto
    LaunchedEffect(Unit) {
        userThemeManager.loadUserPreferences(
            onError = { error ->
                Toast.makeText(context, error, toastDuration).show()
            }
        )

        darkModeEnabled = userThemeManager.isDarkMode.value ?: false
        selectedTextColor = userThemeManager.selectedTextColor.value ?: Color.Black
        backgroundColor = userThemeManager.backgroundColor.value ?: Color.White
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
                .background(backgroundColor)
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
                            userThemeManager.toggleDarkMode(it)

                            // Atualiza o background com base no tema
                            backgroundColor = userThemeManager.backgroundColor.value ?: Color.White

                            coroutineScope.launch {
                                sessionManager.saveUserTheme(if (darkModeEnabled) "DARK" else "LIGHT")
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
                        userThemeManager.updateTextColor(newColor)
                        coroutineScope.launch {
                            sessionManager.saveUserColorScheme("#000000")
                        }
                    })
                    ColorOption(color = Color.Red, onColorSelected = { newColor ->
                        selectedTextColor = newColor
                        userThemeManager.updateTextColor(newColor)
                        coroutineScope.launch {
                            sessionManager.saveUserColorScheme("#FF0000")
                        }
                    })
                    ColorOption(color = Color.Blue, onColorSelected = { newColor ->
                        selectedTextColor = newColor
                        userThemeManager.updateTextColor(newColor)
                        coroutineScope.launch {
                            sessionManager.saveUserColorScheme("#0000FF")
                        }
                    })
                    ColorOption(color = Color.Green, onColorSelected = { newColor ->
                        selectedTextColor = newColor
                        userThemeManager.updateTextColor(newColor)
                        coroutineScope.launch {
                            sessionManager.saveUserColorScheme("#008000")
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

@Composable
fun ColorOption(color: Color, onColorSelected: (Color) -> Unit) {
    Button(
        onClick = { onColorSelected(color) },
        modifier = Modifier
            .size(40.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        // O botão de seleção de cor não precisa de conteúdo
    }
}

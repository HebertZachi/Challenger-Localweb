package br.com.fiap.challengerlocalweb.pages

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.challengerlocalweb.SessionManager
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun userProfile(
    navController: NavController,
    context: Context,
    onLogout: () -> Unit
) {
    val sessionManager = SessionManager.getInstance()
    var fetchedUserName by remember { mutableStateOf("Carregando...") }
    var fetchedUserEmail by remember { mutableStateOf("Carregando...") }
    val coroutineScope = rememberCoroutineScope()

    // Buscar os dados do usuário ao carregar a tela
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            fetchedUserName = sessionManager.fetchUserName() ?: "Nome não encontrado"
            fetchedUserEmail = sessionManager.fetchUserEmail() ?: "E-mail não encontrado"
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
                    icon = { Icon(Icons.Filled.Send, contentDescription = "Emails Enviados") },
                    label = { Text("Enviados") },
                    selected = false,
                    onClick = { navController.navigate("sentItems") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.DateRange, contentDescription = "Calendário") },
                    label = { Text("Calendário") },
                    selected = false,
                    onClick = { navController.navigate("calendar") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = true,
                    onClick = { navController.navigate("userProfile") }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Informações do usuário
                Text(
                    text = "Perfil do Usuário",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Nome do usuário
                Text(
                    text = fetchedUserName,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                // E-mail do usuário
                Text(
                    text = fetchedUserEmail,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Botões de ações
                Button(
                    onClick = { navController.navigate("changePassword") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                ) {
                    Text(text = "Alterar Senha")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { navController.navigate("changeUserName") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                ) {
                    Text(text = "Alterar Nome")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { navController.navigate("userPrefs") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                ) {
                    Text(text = "Preferências")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botão de Logout
                Button(
                    onClick = { onLogout() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Text(text = "Logout")
                }
            }
        }
    }
}

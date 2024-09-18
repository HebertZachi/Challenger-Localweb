package br.com.fiap.challengerlocalweb

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fiap.challengerlocalweb.pages.*
import br.com.fiap.challengerlocalweb.repository.SentEmailRepository
import br.com.fiap.challengerlocalweb.ui.theme.ChallengerLocalWebTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sentEmailRepository = SentEmailRepository(applicationContext)

        // Inicializa o SessionManager com os dados persistidos no SharedPreferences
        SessionManager.initialize(applicationContext)

        setContent {
            ChallengerLocalWebTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val context = applicationContext
                    val sessionManager = SessionManager.getInstance()

                    // Verifica se há um token válido no SessionManager para decidir a tela inicial
                    val userId = sessionManager.fetchUserId() ?: ""
                    val startDestination = if (userId.isNotEmpty()) "inbox" else "Start"

                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable(route = "Start") { StartScreen(navController = navController) }
                        composable(route = "Signup") { signUp(navController = navController) }
                        composable(route = "login") { login(navController = navController, context = context) }
                        composable(route = "inbox") { inbox(navController = navController, context = context) }

                        // Tela de perfil, agora utilizando o SessionManager diretamente
                        composable("userProfile") {
                            userProfile(
                                navController = navController,
                                context = applicationContext,
                                onLogout = {
                                    // Limpa a sessão usando o SessionManager
                                    sessionManager.clearSession()

                                    // Notifica que o cache foi limpo
                                    Toast.makeText(context, "Sessão encerrada com sucesso!", Toast.LENGTH_SHORT).show()

                                    // Redireciona para a tela de login
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true } // Remove o histórico completo
                                    }
                                }
                            )
                        }

                        // Tela de alteração de senha, utilizando o SessionManager diretamente
                        composable(route = "changePassword") {
                            changePassword(
                                navController = navController,
                                context = context
                            )
                        }

                        // Tela de alteração de nome, utilizando o SessionManager diretamente
                        composable(route = "changeUserName") {
                            changeUserName(
                                navController = navController,
                                context = context
                            )
                        }

                        // Tela de preferências do usuário
                        composable(route = "userPrefs") {
                            userPrefs(
                                navController = navController,
                                context = context
                            )
                        }

                        composable(route = "sentItems") {
                            sentItems(
                                navController = navController,
                                sentEmailRepository = sentEmailRepository
                            )
                        }

                        composable(route = "emailCompose") {
                            emailCompose(
                                navController = navController,
                                sentEmailRepository = sentEmailRepository
                            )
                        }

                        composable(route = "calendar") {
                            calendar(navController = navController)
                        }
                    }
                }
            }
        }
    }
}

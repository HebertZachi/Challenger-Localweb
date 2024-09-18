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

                    val userId = sessionManager.fetchUserId() ?: ""
                    val startDestination = if (userId.isNotEmpty()) "inbox" else "start"

                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable(route = "start") { startScreen(navController = navController) }
                        composable(route = "signup") { signup(navController = navController) }
                        composable(route = "signin") { login(navController = navController, context = context) }
                        composable(route = "inbox") { inbox(navController = navController, context = context) }
                        composable("receivedEmailDetail/{emailId}") { backStackEntry ->
                            val emailId = backStackEntry.arguments?.getString("emailId")
                            if (emailId != null) {
                                receivedEmailDetail(navController, emailId, context = context)
                            }
                        }

                        composable(route = "sentItems") {
                            sentEmails(navController = navController, context = context)
                        }

                        composable("sentEmailDetail/{emailId}") { backStackEntry ->
                            val emailId = backStackEntry.arguments?.getString("emailId")
                            if (emailId != null) {
                                sentEmailDetail(navController, emailId, context = context)
                            }
                        }

                        composable(route = "emailCompose") {
                            emailCompose(navController = navController, sentEmailRepository = sentEmailRepository)
                        }

                        composable(route = "calendar") {
                            calendar(navController = navController, context = context)
                        }

                        composable("userProfile") {
                            userProfile(
                                navController = navController,
                                context = applicationContext,
                                onLogout = {
                                    sessionManager.clearSession()
                                    Toast.makeText(context, "Sessão encerrada com sucesso!", Toast.LENGTH_SHORT).show()
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(route = "changePassword") {
                            changePassword(navController = navController)
                        }

                        composable(route = "changeUserName") {
                            changeUserName(navController = navController, context = context)
                        }

                        composable(route = "userPrefs") {
                            userPrefs(navController = navController, context = context)
                        }
                    }
                }
            }
        }
    }
}

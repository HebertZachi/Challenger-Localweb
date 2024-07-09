package br.com.fiap.challengerlocalweb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.fiap.challengerlocalweb.pages.*
import br.com.fiap.challengerlocalweb.repository.SentEmailRepository
import br.com.fiap.challengerlocalweb.ui.theme.ChallengerLocalWebTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sentEmailRepository = SentEmailRepository(applicationContext)

        setContent {
            ChallengerLocalWebTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {
                        composable(route = "login") { login(navController = navController) }
                        composable(route = "inbox") { inbox(navController = navController, context = applicationContext) }
                        composable(route = "userProfile") { userProfile(navController = navController) }
                        composable(route = "changePassword") { changePassword(navController = navController) }
                        composable(route = "addAccount") { setNewAccountType(navController = navController) }
                        composable(route = "sentItems") { sentItems(navController = navController, sentEmailRepository = sentEmailRepository) }
                        composable(route = "emailCompose") { emailCompose(navController = navController, sentEmailRepository = sentEmailRepository) }
                        composable(route = "calendar") { calendar(navController = navController) }
                        composable(route = "newAccountManually") { newAccountManually(navController = navController) }
                        composable(
                            route = "newAccountOutsourcing/{provider}",
                            arguments = listOf(navArgument("provider") { type = NavType.StringType })
                        ) { backStackEntry ->
                            newAccountOutsourcing(navController = navController, backStackEntry = backStackEntry)
                        }
                        composable(
                            route = "editAccountManually/{email}",
                            arguments = listOf(navArgument("email") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email") ?: ""
                            editAccountManually(navController = navController, email = email)
                        }
                    }
                }
            }
        }
    }
}

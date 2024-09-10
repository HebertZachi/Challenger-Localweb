package br.com.fiap.challengerlocalweb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fiap.challengerlocalweb.pages.emailCompose
import br.com.fiap.challengerlocalweb.pages.inbox
import br.com.fiap.challengerlocalweb.pages.sentItems
import br.com.fiap.challengerlocalweb.repository.SentEmailRepository
import br.com.fiap.challengerlocalweb.ui.theme.ChallengerLocalWebTheme
import calendar

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
                        startDestination = "inbox"
                    ) {
                        composable(route = "inbox") { inbox(navController = navController, context = applicationContext) }
                        composable(route = "sentItems") { sentItems(navController = navController, sentEmailRepository = sentEmailRepository) }
                        composable(route = "emailCompose") { emailCompose(navController = navController, sentEmailRepository = sentEmailRepository) }
                        composable(route = "calendar") {
                            calendar(navController = navController, context = applicationContext)
                        }
                    }
                }
            }
        }
    }
}

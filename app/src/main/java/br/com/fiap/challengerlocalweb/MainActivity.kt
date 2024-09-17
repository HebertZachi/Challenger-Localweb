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
import br.com.fiap.challengerlocalweb.pages.receivedEmailDetail
import br.com.fiap.challengerlocalweb.pages.sentEmailDetail
import br.com.fiap.challengerlocalweb.pages.sentEmails
import br.com.fiap.challengerlocalweb.ui.theme.ChallengerLocalWebTheme
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
                        composable(route = "sentEmails") { sentEmails(navController = navController, context = applicationContext) }
                        composable(route = "emailCompose") { emailCompose(navController = navController, sentEmailRepository = sentEmailRepository) }
                        composable(route = "calendar") { calendar(navController = navController, context = applicationContext) }
                        composable("receivedEmailDetail/{emailId}") { backStackEntry ->
                            val emailId = backStackEntry.arguments?.getString("emailId")?.toLongOrNull()
                            if (emailId != null) {
                                receivedEmailDetail(navController, emailId, context = applicationContext)
                            } else {
                            }
                        }
                        composable("sentEmailDetail/{emailId}") { backStackEntry ->
                            val emailId = backStackEntry.arguments?.getString("emailId")?.toLongOrNull()
                            if (emailId != null) {
                                sentEmailDetail(navController, emailId, context = applicationContext)
                            } else {
                            }
                        }
                        composable(route = "calendar") {
                            calendar(navController = navController, context = applicationContext)
                        }
                    }
                }
            }
        }
    }
}

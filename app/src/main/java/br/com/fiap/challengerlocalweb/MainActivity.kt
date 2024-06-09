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
import br.com.fiap.challengerlocalweb.pages.calendar
import br.com.fiap.challengerlocalweb.pages.emailCompose
import br.com.fiap.challengerlocalweb.pages.emailViewer
import br.com.fiap.challengerlocalweb.pages.inbox
import br.com.fiap.challengerlocalweb.pages.sentItems
import br.com.fiap.challengerlocalweb.ui.theme.ChallengerLocalWebTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengerLocalWebTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "inbox"){
                            composable(route = "inbox") { inbox(navController) }
                            composable(route = "sentItems") { sentItems(navController) }
                            composable(route = "emailCompose") { emailCompose(navController) }
                            composable(route = "emailViewer") { emailViewer(navController) }
                            composable(route = "calendar") { calendar(navController) }
                    }
                }
            }


        }
    }
}
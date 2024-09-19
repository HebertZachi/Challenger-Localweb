package br.com.fiap.challengerlocalweb.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.challengerlocalweb.R

@Composable
fun startScreen(navController: NavController) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val logoSize = (screenWidth.coerceAtMost(screenHeight) * 0.5f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.wave_logo),
            contentDescription = "WaveMail Logo",
            modifier = Modifier.size(logoSize)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append("Wave")
                }
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                    append("mail")
                }
            },
            fontSize = (logoSize.value / 3).sp,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Button(
            onClick = { navController.navigate("login") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Já tem conta? Faça Login", color = MaterialTheme.colorScheme.onPrimary)
        }

        Button(
            onClick = { navController.navigate("signup") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
        ) {
            Text(text = "Ainda não tem conta? Cadastre-se", color = MaterialTheme.colorScheme.onSecondary)
        }
    }
}

package br.com.fiap.challengerlocalweb.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.challengerlocalweb.R

@Composable
fun setNewAccountType(navController: NavController) {
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
                .background(Color(0xFF253645))
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Adicionar Conta de E-mail",
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("newAccountManually") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Adicionar Manualmente")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Logar com Provedor:",
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                AccountProviderButton(
                    navController = navController,
                    label = "Logar com Google",
                    icon = R.drawable.ic_google,
                    provider = "google"
                )

                Spacer(modifier = Modifier.height(16.dp))

                AccountProviderButton(
                    navController = navController,
                    label = "Logar com Outlook",
                    icon = R.drawable.ic_outlook,
                    provider = "outlook"
                )

                Spacer(modifier = Modifier.height(16.dp))

                AccountProviderButton(
                    navController = navController,
                    label = "Logar com Yahoo",
                    icon = R.drawable.ic_yahoo,
                    provider = "yahoo"
                )

                // Adicione mais provedores conforme necessário
            }
        }
    }
}

@Composable
fun AccountProviderButton(navController: NavController, label: String, icon: Int, provider: String) {
    Button(
        onClick = { navController.navigate("newAccountOutsourcing/$provider") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, color = Color.Black, fontSize = 16.sp)
        }
    }
}

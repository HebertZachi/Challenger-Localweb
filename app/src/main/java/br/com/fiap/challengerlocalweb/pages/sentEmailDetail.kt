package br.com.fiap.challengerlocalweb.pages

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.challengerlocalweb.model.SentEmail
import br.com.fiap.challengerlocalweb.repository.SentEmailRepository
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@Composable
fun sentEmailDetail(navController: NavController, emailId: Long?, context: Context) {
    val sentEmailRepository = remember { SentEmailRepository(context) }
    var email by remember { mutableStateOf<SentEmail?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var isRecipientExpanded by remember { mutableStateOf(false) }
    var isCcExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(emailId) {
        coroutineScope.launch {
            email = emailId?.let { sentEmailRepository.findById(it) }
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Email, contentDescription = "Emails Recebidos") },
                    label = { Text("Recebidos") },
                    selected = navController.currentDestination?.route == "inbox",
                    onClick = {
                        if (navController.currentDestination?.route != "inbox") {
                            navController.navigate("inbox")
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Send, contentDescription = "Emails Enviados") },
                    label = { Text("Enviados") },
                    selected = navController.currentDestination?.route == "sentEmails",
                    onClick = {
                        if (navController.currentDestination?.route != "sentEmails") {
                            navController.navigate("sentEmails")
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.DateRange, contentDescription = "Calendário") },
                    label = { Text("Calendário") },
                    selected = navController.currentDestination?.route == "calendar",
                    onClick = {
                        if (navController.currentDestination?.route != "calendar") {
                            navController.navigate("calendar")
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = navController.currentDestination?.route == "profile",
                    onClick = {
                        if (navController.currentDestination?.route != "profile") {
                            navController.navigate("profile")
                        }
                    }
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
            email?.let { email ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "${email.baseEmail.subject}",
                        fontSize = 24.sp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isRecipientExpanded = !isRecipientExpanded }
                            .background(Color(0xFF3C4A60), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Para: ${email.recipient}",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        if (isRecipientExpanded) {
                            Text(
                                text = email.recipient,
                                fontSize = 14.sp,
                                color = Color.LightGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isCcExpanded = !isCcExpanded }
                            .background(Color(0xFF3C4A60), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Cc: ${email.baseEmail.cc}",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        if (isCcExpanded) {
                            Text(
                                text = email.baseEmail.cc,
                                fontSize = 14.sp,
                                color = Color.LightGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = email.baseEmail.body,
                        fontSize = 16.sp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    val formattedDate = email.creationDate.format(dateFormatter)
                    Text(
                        text = "Enviado em: $formattedDate",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}

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
import br.com.fiap.challengerlocalweb.relations.SentEmailWithUsers
import br.com.fiap.challengerlocalweb.repository.SentEmailRepository
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@Composable
fun sentEmailDetail(navController: NavController, emailId: String?, context: Context) {
    val sentEmailRepository = remember { SentEmailRepository(context) }
    var email by remember { mutableStateOf<SentEmailWithUsers?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var isRecipientExpanded by remember { mutableStateOf(false) }
    var isCcExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(emailId) {
        coroutineScope.launch {
            email = emailId?.let { sentEmailRepository.getSentEmailById(it) }
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
                        text = "${email.sentEmail.subject}",
                        fontSize = 24.sp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    val receivers = email.receivers
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isRecipientExpanded = !isRecipientExpanded }
                            .background(Color(0xFF3C4A60), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        if (receivers.isNotEmpty()) {
                            Text(
                                text = "Para: ${receivers.first()}",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            if (receivers.size > 1) {
                                Text(
                                    text = "+${receivers.size - 1} mais",
                                    fontSize = 14.sp,
                                    color = Color.LightGray
                                )
                            }
                            if (isRecipientExpanded) {
                                receivers.drop(1).forEach { receiver ->
                                    Text(
                                        text = receiver.userEmailId,
                                        fontSize = 14.sp,
                                        color = Color.LightGray
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val ccList = email.cc
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isCcExpanded = !isCcExpanded }
                            .background(Color(0xFF3C4A60), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        if (ccList.isNotEmpty()) {
                            Text(
                                text = "Cc: ${ccList.first()}",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            if (ccList.size > 1) {
                                Text(
                                    text = "+${ccList.size - 1} mais",
                                    fontSize = 14.sp,
                                    color = Color.LightGray
                                )
                            }
                            if (isCcExpanded) {
                                ccList.drop(1).forEach { cc ->
                                    Text(
                                        text = cc.userEmailId,
                                        fontSize = 14.sp,
                                        color = Color.LightGray
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = email.sentEmail.body,
                        fontSize = 16.sp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    val formattedDate = email.sentEmail.createdAt.format(dateFormatter)
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

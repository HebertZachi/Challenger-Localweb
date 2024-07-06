package br.com.fiap.challengerlocalweb.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.challengerlocalweb.model.Email
import br.com.fiap.challengerlocalweb.model.SentEmail
import br.com.fiap.challengerlocalweb.repository.SentEmailRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
fun emailCompose(navController: NavController, sentEmailRepository: SentEmailRepository) {
    var recipient by remember { mutableStateOf(TextFieldValue("")) }
    var cc by remember { mutableStateOf(TextFieldValue("")) }
    var subject by remember { mutableStateOf(TextFieldValue("")) }
    var body by remember { mutableStateOf(TextFieldValue("")) }
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

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
                    onClick = { navController.navigate("profile") }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(Color(0xFF253645))
                .fillMaxSize()
                .padding(innerPadding)
                .clickable { focusManager.clearFocus() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Novo E-mail",
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .align(Alignment.Start),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 22.sp,
                )

                OutlinedTextField(
                    value = recipient,
                    onValueChange = { recipient = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    shape = RoundedCornerShape(8.dp),
                    textStyle = TextStyle(color = Color.White),
                    label = { Text("Para", color = Color.White) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
                )

                OutlinedTextField(
                    value = cc,
                    onValueChange = { cc = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    shape = RoundedCornerShape(8.dp),
                    textStyle = TextStyle(color = Color.White),
                    label = { Text("Cc", color = Color.White) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
                )

                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    shape = RoundedCornerShape(8.dp),
                    textStyle = TextStyle(color = Color.White),
                    label = { Text("Assunto", color = Color.White) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
                )

                OutlinedTextField(
                    value = body,
                    onValueChange = { body = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 10.dp),
                    shape = RoundedCornerShape(8.dp),
                    textStyle = TextStyle(color = Color.White),
                    label = { Text("Corpo do e-mail", color = Color.White) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    maxLines = 5
                )

                Button(
                    onClick = {
                        val newEmail = SentEmail(
                            baseEmail = Email(
                                cc = cc.text,
                                subject = subject.text,
                                body = body.text
                            ),
                            recipient = recipient.text,
                            creationDate = LocalDateTime.now()
                        )

                        coroutineScope.launch {
                            sentEmailRepository.save(newEmail)
                        }

                        navController.navigate("inbox")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(text = "Enviar", fontSize = 18.sp)
                }
            }
        }
    }
}


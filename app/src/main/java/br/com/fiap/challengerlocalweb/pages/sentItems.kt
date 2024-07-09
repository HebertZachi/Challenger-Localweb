package br.com.fiap.challengerlocalweb.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.challengerlocalweb.model.SentEmail
import br.com.fiap.challengerlocalweb.repository.SentEmailRepository
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@Composable
fun sentItems(navController: NavController, sentEmailRepository: SentEmailRepository) {
    var searchQuery by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()
    var emails by remember { mutableStateOf(listOf<SentEmail>()) }

    LaunchedEffect(Unit) {
        emails = sentEmailRepository.findAll()
    }

    val filteredEmails = emails.filter { email ->
        if (searchActive) {
            email.baseEmail.subject.contains(searchQuery, ignoreCase = true) ||
                    email.baseEmail.body.contains(searchQuery, ignoreCase = true) ||
                    email.recipient.contains(searchQuery, ignoreCase = true)
        } else {
            true
        }
    }

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
                    selected = true,
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
                    selected = false,
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
                .clickable {
                    focusManager.clearFocus()
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .onFocusChanged { focusState ->
                            searchActive = focusState.isFocused
                        },
                    shape = RoundedCornerShape(50.dp),
                    textStyle = TextStyle(color = Color.White),
                    label = {
                        Text(
                            text = "Search",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            searchActive = true
                            focusManager.clearFocus()
                        }
                    )
                )

                Text(
                    text = "Itens Enviados",
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .align(Alignment.Start), // Alinhamento à esquerda
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 22.sp,
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    items(filteredEmails) { email ->
                        SentEmailItem(email)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            FloatingActionButton(
                onClick = { navController.navigate("emailCompose") },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Criar novo e-mail"
                )
            }
        }
    }
}

@Composable
fun SentEmailItem(email: SentEmail) {
    Button(
        onClick = { /* Navegar para a tela de detalhes do e-mail */ },
        modifier = Modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(Color(0xFF3C4A60)),
        shape = RoundedCornerShape(10.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = email.baseEmail.subject,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val formattedDate = email.creationDate.format(dateFormatter)
                Text(
                    text = formattedDate,
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Text(
                text = "Para: ${email.recipient}",
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth()
            )

            Text(
                text = email.baseEmail.body,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}
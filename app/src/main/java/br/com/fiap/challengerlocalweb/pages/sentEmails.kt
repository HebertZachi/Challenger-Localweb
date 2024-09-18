package br.com.fiap.challengerlocalweb.pages

import android.content.Context
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
import br.com.fiap.challengerlocalweb.relations.SentEmailWithUsers
import br.com.fiap.challengerlocalweb.repository.SentEmailRepository

@Composable
fun sentEmails(navController: NavController, context: Context) {
    var searchQuery by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val sentEmailRepository = SentEmailRepository(context)
    val coroutineScope = rememberCoroutineScope()
    var emails by remember { mutableStateOf(listOf<SentEmailWithUsers>()) }

    LaunchedEffect(Unit) {
        emails = sentEmailRepository.getAllSentEmails()
    }

//    LaunchedEffect(Unit) {
//        coroutineScope.launch {
//            val existingEmails = sentEmailRepository.findAll()
//            if (existingEmails.isEmpty()) {
//                val sampleEmails = sampleReceivedEmails()
//                sampleEmails.forEach { email ->
//                    sentEmailRepository.save(email)
//                }
//                emails = sentEmailRepository.findAll()
//            } else {
//                emails = existingEmails
//            }
//        }
//    }

//    LaunchedEffect(Unit) {
//        coroutineScope.launch {
//            val existingEmails = sentEmailRepository.getAllSentEmails()
//            if (existingEmails.isEmpty()) {
//                val sampleEmails = sampleReceivedEmails()
//
//                sampleEmails.forEach { (email, recipients) ->
//                    val receivers = recipients["receivers"] ?: emptyList()
//                    val cc = recipients["cc"] ?: emptyList()
//                    sentEmailRepository.insertSentEmail(email, receivers, cc)
//                }
//
//                emails = sentEmailRepository.getAllSentEmails()
//            } else {
//                emails = existingEmails
//            }
//        }
//    }

    val filteredEmails = emails.filter { email ->
        if (searchActive) {
            email.sentEmail.subject.contains(searchQuery, ignoreCase = true) ||
                    email.sentEmail.body.contains(searchQuery, ignoreCase = true) ||
                    email.receivers.any { receiver -> receiver.userEmailId.contains(searchQuery, ignoreCase = true) }
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
                    onClick = { navController.navigate("userProfile") }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
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
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    label = {
                        Text(
                            text = "Search",
                            color = MaterialTheme.colorScheme.onBackground,
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
                        .align(Alignment.Start),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 22.sp,
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    items(filteredEmails) { email ->
                        sentEmailItem(email, navController)
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
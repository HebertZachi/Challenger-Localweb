package br.com.fiap.challengerlocalweb.pages

import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.challengerlocalweb.SessionManager
import br.com.fiap.challengerlocalweb.api.EmailApiService
import br.com.fiap.challengerlocalweb.api.RetrofitFactory
import br.com.fiap.challengerlocalweb.relations.ReceivedEmailWithUsers
import br.com.fiap.challengerlocalweb.repository.ReceivedEmailRepository
import br.com.fiap.challengerlocalweb.utils.mapToReceivedEmail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun inbox(navController: NavController) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val receivedEmailRepository = ReceivedEmailRepository(context)
    val coroutineScope = rememberCoroutineScope()
    var emails by remember { mutableStateOf(listOf<ReceivedEmailWithUsers>()) }

    val apiService = RetrofitFactory.getRetrofit(context).create(EmailApiService::class.java)

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val existingEmails = receivedEmailRepository.getAllEmails()
                if (existingEmails.isEmpty()) {
                    val userId = SessionManager.getInstance().fetchUserId()
                    val response = apiService.getAllReceivedEmails(userId).execute()
                    Log.d("BACKEND", "Response code: ${response.code()}, message: ${response.message()}")
                    if (response.isSuccessful) {
                        response.body()?.let { pageResponse ->
                            val fetchedEmails = pageResponse.content
                            fetchedEmails?.forEach { emailDto ->
                                val email = mapToReceivedEmail(emailDto)
                                val receivers = emailDto.to
                                val cc = emailDto.cc
                                receivedEmailRepository.insertEmail(email, receivers, cc)
                            }
                            emails = receivedEmailRepository.getAllEmails()
                        }
                    } else {
                        Log.e("BACKEND", "Error: ${response.code()} - ${response.message()}")
                    }
                } else {
                    emails = existingEmails
                }
            }
        }
    }

    val filteredEmails = emails.filter { email ->
        if (searchActive) {
            email.receivedEmail.senderEmail.contains(searchQuery, ignoreCase = true) ||
                    email.receivedEmail.subject.contains(searchQuery, ignoreCase = true) ||
                    email.receivedEmail.body.contains(searchQuery, ignoreCase = true)
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
                    selected = true,
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
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                    label = {
                        Text(
                            text = "Search",
                            color = MaterialTheme.colorScheme.onSurface,
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
                    text = "Caixa de entrada",
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
                        receivedEmailItem(email, navController)
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

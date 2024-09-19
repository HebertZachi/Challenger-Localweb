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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.challengerlocalweb.model.ReceivedEmail
import br.com.fiap.challengerlocalweb.relations.ReceivedEmailWithUsers
import br.com.fiap.challengerlocalweb.repository.ReceivedEmailRepository
import br.com.fiap.challengerlocalweb.theme.UserThemeManager
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
fun inbox(navController: NavController, context: Context, userThemeManager: UserThemeManager) {
    var searchQuery by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val receivedEmailRepository = ReceivedEmailRepository(context)
    val coroutineScope = rememberCoroutineScope()
    var emails by remember { mutableStateOf(listOf<ReceivedEmailWithUsers>()) }

    val backgroundColor = userThemeManager.backgroundColor.value
    val textColor = userThemeManager.selectedTextColor.value

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val existingEmails = receivedEmailRepository.getAllEmails()
            if (existingEmails.isEmpty()) {
                val sampleEmails = sampleReceivedEmails()

                sampleEmails.forEach { (email, recipients) ->
                    val receivers = recipients["receivers"] ?: emptyList()
                    val cc = recipients["cc"] ?: emptyList()
                    receivedEmailRepository.insertEmail(email, receivers, cc)
                }

                emails = receivedEmailRepository.getAllEmails()
            } else {
                emails = existingEmails
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
                    label = { Text("Recebidos", style = TextStyle(color = textColor)) },
                    selected = true,
                    onClick = { navController.navigate("inbox") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Send, contentDescription = "Emails Enviados") },
                    label = { Text("Enviados", style = TextStyle(color = textColor)) },
                    selected = false,
                    onClick = { navController.navigate("sentItems") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.DateRange, contentDescription = "Calendário") },
                    label = { Text("Calendário", style = TextStyle(color = textColor)) },
                    selected = false,
                    onClick = { navController.navigate("calendar") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil", style = TextStyle(color = textColor)) },
                    selected = false,
                    onClick = { navController.navigate("userProfile") }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(backgroundColor)
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
                    textStyle = TextStyle(color = textColor),
                    label = {
                        Text(
                            text = "Search",
                            color = textColor,
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
                    color = textColor,
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
                    contentDescription = "Criar novo e-mail",
                    tint = Color.White
                )
            }
        }
    }
}
fun sampleReceivedEmails(): HashMap<ReceivedEmail, HashMap<String, List<String>>> {
    val emailMap = hashMapOf<ReceivedEmail, HashMap<String, List<String>>>()

    emailMap[ReceivedEmail(
        receivedEmailId = "email1",
        subject = "Reunião de Projeto",
        senderEmail = "paula.martins@example.com",
        body = "Não se esqueça da reunião de projeto amanhã às 15h.",
        createdAt = LocalDateTime.now().minusDays(1)
    )] = hashMapOf(
        "receivers" to listOf("joao.silva@example.com", "lucas.garcia@example.com"),
        "cc" to listOf("adriana.oliveira@example.com")
    )

    emailMap[ReceivedEmail(
        receivedEmailId = "email2",
        subject = "Documentos Importantes",
        senderEmail = "roberto.pereira@example.com",
        body = "Os documentos importantes estão anexados para sua revisão.",
        createdAt = LocalDateTime.now().minusDays(2)
    )] = hashMapOf(
        "receivers" to listOf("julia.souza@example.com"),
        "cc" to listOf("sergio.lima@example.com")
    )

    emailMap[ReceivedEmail(
        receivedEmailId = "email3",
        subject = "Resumo Semanal",
        senderEmail = "marta.nunes@example.com",
        body = "Aqui está o resumo semanal das atividades.",
        createdAt = LocalDateTime.now().minusDays(3)
    )] = hashMapOf(
        "receivers" to listOf("mario.santos@example.com", "carla.silva@example.com"),
        "cc" to emptyList()
    )

    emailMap[ReceivedEmail(
        receivedEmailId = "email4",
        subject = "Alerta de Sistema",
        senderEmail = "suporte.tecnico@example.com",
        body = "Um alerta do sistema foi gerado e precisa da sua atenção.",
        createdAt = LocalDateTime.now().minusDays(4)
    )] = hashMapOf(
        "receivers" to listOf("nelson.almeida@example.com"),
        "cc" to listOf("lucas.garcia@example.com", "patricia.santos@example.com")
    )

    emailMap[ReceivedEmail(
        receivedEmailId = "email5",
        subject = "Convite para Webinar",
        senderEmail = "eventos@example.com",
        body = "Você está convidado para o nosso webinar na próxima semana.",
        createdAt = LocalDateTime.now().minusDays(5)
    )] = hashMapOf(
        "receivers" to listOf("ana.santos@example.com", "marcio.martins@example.com"),
        "cc" to listOf("daniela.morais@example.com")
    )

    emailMap[ReceivedEmail(
        receivedEmailId = "email6",
        subject = "Atualização de Política",
        senderEmail = "rh@example.com",
        body = "Leia a nova política de privacidade atualizada.",
        createdAt = LocalDateTime.now().minusDays(6)
    )] = hashMapOf(
        "receivers" to listOf("fernando.pereira@example.com"),
        "cc" to listOf("beatriz.lima@example.com", "roberto.pereira@example.com")
    )

    emailMap[ReceivedEmail(
        receivedEmailId = "email7",
        subject = "Oferta Especial",
        senderEmail = "vendas@example.com",
        body = "Aproveite a oferta especial disponível apenas para hoje.",
        createdAt = LocalDateTime.now().minusDays(7)
    )] = hashMapOf(
        "receivers" to listOf("silvia.oliveira@example.com", "bruno.azevedo@example.com"),
        "cc" to emptyList()
    )

    emailMap[ReceivedEmail(
        receivedEmailId = "email8",
        subject = "Solicitação de Feedback",
        senderEmail = "gerencia@example.com",
        body = "Por favor, forneça seu feedback sobre o último projeto.",
        createdAt = LocalDateTime.now().minusDays(8)
    )] = hashMapOf(
        "receivers" to listOf("gabriela.souza@example.com", "carlos.martins@example.com"),
        "cc" to listOf("patricia.costa@example.com")
    )

    emailMap[ReceivedEmail(
        receivedEmailId = "email9",
        subject = "Novo Relatório Disponível",
        senderEmail = "financeiro@example.com",
        body = "O novo relatório financeiro está disponível para download.",
        createdAt = LocalDateTime.now().minusDays(9)
    )] = hashMapOf(
        "receivers" to listOf("rafael.dias@example.com"),
        "cc" to listOf("julia.oliveira@example.com", "eduardo.lima@example.com")
    )

    emailMap[ReceivedEmail(
        receivedEmailId = "email10",
        subject = "Confirmação de Reunião",
        senderEmail = "agenda@example.com",
        body = "Sua reunião foi confirmada para amanhã às 14h.",
        createdAt = LocalDateTime.now().minusDays(10)
    )] = hashMapOf(
        "receivers" to listOf("laura.souza@example.com", "marcio.lopes@example.com"),
        "cc" to emptyList()
    )

    return emailMap
}
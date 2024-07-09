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
import br.com.fiap.challengerlocalweb.AppDatabase
import br.com.fiap.challengerlocalweb.model.Email
import br.com.fiap.challengerlocalweb.model.ReceivedEmail
import br.com.fiap.challengerlocalweb.repository.ReceivedEmailRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun inbox(navController: NavController, context: Context) {
    var searchQuery by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val receivedEmailRepository = ReceivedEmailRepository(context)
    val coroutineScope = rememberCoroutineScope()
    var emails by remember { mutableStateOf(listOf<ReceivedEmail>()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val existingEmails = receivedEmailRepository.findAll()
            if (existingEmails.isEmpty()) {
                val sampleEmails = sampleReceivedEmails()
                sampleEmails.forEach { email ->
                    receivedEmailRepository.save(email)
                }
                emails = receivedEmailRepository.findAll()
            } else {
                emails = existingEmails
            }
        }
    }

    val filteredEmails = emails.filter { email ->
        if (searchActive) {
            email.sender.contains(searchQuery, ignoreCase = true) ||
                    email.baseEmail.subject.contains(searchQuery, ignoreCase = true) ||
                    email.baseEmail.body.contains(searchQuery, ignoreCase = true)
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
                    text = "Caixa de entrada",
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .align(Alignment.Start),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 22.sp,
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    items(filteredEmails) { email ->
                        EmailItem(email)
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
fun EmailItem(email: ReceivedEmail) {
    Button(
        onClick = {},
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
                val formattedDate = email.receivedDate.format(dateFormatter)
                Text(
                    text = formattedDate,
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

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

fun sampleReceivedEmails() = listOf(
    ReceivedEmail(baseEmail = Email(subject = "Lembrete de Reunião", body = "Não esqueça da nossa reunião às 10h amanhã."), sender = "joao.silva@example.com", receivedDate = LocalDateTime.now().minusDays(1)),
    ReceivedEmail(baseEmail = Email(subject = "Atualização de Projeto", body = "As últimas atualizações do projeto estão anexadas."), sender = "ana.santos@example.com", receivedDate = LocalDateTime.now()),
    ReceivedEmail(baseEmail = Email(subject = "Você ganhou um prêmio!", body = "Clique aqui para resgatar seu prêmio."), sender = "promo.bot@example.com", receivedDate = LocalDateTime.now().minusDays(2)),
    ReceivedEmail(baseEmail = Email(subject = "Convite para Evento", body = "Está convidado para o evento especial na sexta-feira."), sender = "eventos@example.com", receivedDate = LocalDateTime.now().minusDays(3)),
    ReceivedEmail(baseEmail = Email(subject = "Confirmação de Inscrição", body = "Sua inscrição foi confirmada para o curso de fotografia."), sender = "cursos@example.com", receivedDate = LocalDateTime.now().minusDays(4)),
    ReceivedEmail(baseEmail = Email(subject = "Revisão de Contrato", body = "Por favor, revise o contrato anexado e forneça seu feedback."), sender = "legal@example.com", receivedDate = LocalDateTime.now().minusDays(5)),
    ReceivedEmail(baseEmail = Email(subject = "Atualização de Software", body = "Uma nova atualização de software está disponível para download."), sender = "tech.support@example.com", receivedDate = LocalDateTime.now().minusDays(6)),
    ReceivedEmail(baseEmail = Email(subject = "Promoção de Aniversário", body = "Celebre seu aniversário conosco e ganhe descontos exclusivos."), sender = "promocoes@example.com", receivedDate = LocalDateTime.now().minusDays(7)),
    ReceivedEmail(baseEmail = Email(subject = "Lembrete de Pagamento", body = "Não se esqueça de efetuar o pagamento da fatura até o dia 30/06."), sender = "financeiro@example.com", receivedDate = LocalDateTime.now().minusDays(8)),
    ReceivedEmail(baseEmail = Email(subject = "Novo Catálogo Disponível", body = "Explore nosso novo catálogo de produtos e aproveite as ofertas especiais."), sender = "marketing@example.com", receivedDate = LocalDateTime.now().minusDays(9))
)


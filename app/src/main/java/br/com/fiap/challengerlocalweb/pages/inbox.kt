package br.com.fiap.challengerlocalweb.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class Email(
    val sender: String,
    val subject: String,
    val body: String,
    val timestamp: String
)

@Composable
fun inbox(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val emails = sampleEmails().filter { email ->
        email.sender.contains(searchQuery, ignoreCase = true) ||
                email.subject.contains(searchQuery, ignoreCase = true) ||
                email.body.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .background(Color(0xFF253645))
            .fillMaxSize()
            .padding(10.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            shape = RoundedCornerShape(50.dp),
            placeholder = {
                Text(
                    text = "Search",
                    color = Color.White,
                )
            }
        )

        Text(
            text = "Caixa de entrada",
            modifier = Modifier.padding(vertical = 10.dp),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 22.sp,
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(emails) { email ->
                EmailItem(email)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun EmailItem(email: Email) {
    Button(
        onClick = { /* Navegar para a tela de detalhes do e-mail */ },
        modifier = Modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(Color(0xFF3C4A60)),
        shape = RoundedCornerShape(10.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(text = email.sender, color = Color.White, fontWeight = FontWeight.Bold)
                Text(text = email.subject, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = email.timestamp, color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

fun sampleEmails() = listOf(
    Email("John Doe", "Meeting Reminder Reminder2 Reminder3 Reminder4", "Don't forget our meeting at 10 AM tomorrow.", "Yesterday"),
    Email("Jane Smith", "Project Update", "The latest project updates are attached.", "Today"),
    Email("Spam Bot", "You've won a prize!", "Click here to claim your prize.", "2 days ago"),
)

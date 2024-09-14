import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@Composable
fun calendar(navController: NavController, context: Context) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var showMeetingDialog by remember { mutableStateOf(false) }

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
                    selected = true,
                    onClick = { }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Exibe o mês e o ano
            Text(
                text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR"))} ${currentMonth.year}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(8.dp)
            )

            // Navegação entre os meses
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                    Text("Anterior")
                }
                Button(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                    Text("Próximo")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dias da semana
            Row(modifier = Modifier.fillMaxWidth()) {
                val daysOfWeek = listOf("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb")
                for (day in daysOfWeek) {
                    Text(
                        text = day,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Exibição do calendário
            LazyColumn {
                items(getWeeksOfMonth(currentMonth)) { week ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        for (day in week) {
                            val isCurrentMonth = day?.month == currentMonth.month
                            val isSelected = day == selectedDate

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp)
                                    .aspectRatio(1f)
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary
                                        else if (!isCurrentMonth) Color.LightGray
                                        else Color.Transparent,
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .clickable {
                                        selectedDate = day ?: selectedDate
                                        if (selectedDate == LocalDate.now()) {
                                            showMeetingDialog = true
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day?.dayOfMonth?.toString() ?: "",
                                    color = if (isSelected) Color.White else Color.Black,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }

        // Modal exibindo compromisso ao clicar no dia atual
        if (showMeetingDialog) {
            AlertDialog(
                onDismissRequest = { showMeetingDialog = false },
                title = {
                    Text(text = "Reunião Agendada")
                },
                text = {
                    Column {
                        Text("Data: ${selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}")
                        Text("Horário: 10h")
                        Text("Local: Sala de Reuniões 1")
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showMeetingDialog = false }) {
                        Text("Fechar")
                    }
                }
            )
        }
    }
}

fun getWeeksOfMonth(yearMonth: YearMonth): List<List<LocalDate?>> {
    val firstDayOfMonth = yearMonth.atDay(1)
    val lastDayOfMonth = yearMonth.atEndOfMonth()

    val firstDayOfWeek = firstDayOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))

    val days = mutableListOf<LocalDate?>()
    var currentDay = firstDayOfWeek

    while (currentDay <= lastDayOfMonth.plusWeeks(1)) {
        days.add(currentDay)
        currentDay = currentDay.plusDays(1)
    }

    val weeks = days.chunked(7)

    if (weeks.isNotEmpty() && weeks.last().all { it?.month != yearMonth.month }) {
        return weeks.dropLast(1)
    }

    return weeks
}

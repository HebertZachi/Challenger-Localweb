import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.fiap.challengerlocalweb.model.Event
import br.com.fiap.challengerlocalweb.repository.EventRepository
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun calendar(navController: NavController, context: Context) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var showAddEventDialog by remember { mutableStateOf(false) }
    var showMeetingDialog by remember { mutableStateOf(false) }
    var eventTitle by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }
    var eventStartDateTime by remember { mutableStateOf(LocalDateTime.now()) }
    var eventEndDateTime by remember { mutableStateOf(LocalDateTime.now().plusHours(1)) }
    var isAllDay by remember { mutableStateOf(false) }
    var isRecurring by remember { mutableStateOf(false) }
    var recurrenceRule by remember { mutableStateOf("") }

    val eventRepository = EventRepository(context)
    val scope = rememberCoroutineScope()

    val events by eventRepository.getEventsBetween(
        start = currentMonth.atDay(1).atStartOfDay(),
        end = currentMonth.atEndOfMonth().atTime(23, 59, 59)
    ).collectAsState(initial = emptyList())

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
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddEventDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Evento")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Calendar section (50% of screen height)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR"))} ${currentMonth.year}",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(8.dp)
                    )

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

                    LazyColumn {
                        item {
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
                        }

                        items(getWeeksOfMonth(currentMonth)) { week ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                for (day in week) {
                                    val isCurrentMonth = day?.month == currentMonth.month
                                    val isSelected = day == selectedDate
                                    val hasEvent = events.any { it.startDateTime.toLocalDate() == day }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(4.dp)
                                            .aspectRatio(1f)
                                            .background(
                                                if (isSelected) MaterialTheme.colorScheme.primary
                                                else if (!isCurrentMonth) Color.LightGray
                                                else if (hasEvent) Color.Green
                                                else Color.Transparent,
                                                shape = MaterialTheme.shapes.small
                                            )
                                            .clickable {
                                                selectedDate = day ?: selectedDate
                                                if (hasEvent) {
                                                    showMeetingDialog = true
                                                } else if (day != null) {
                                                    showAddEventDialog = true
                                                    eventTitle = ""
                                                    eventDescription = ""
                                                    eventLocation = ""
                                                    isAllDay = false
                                                    isRecurring = false
                                                    recurrenceRule = ""
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
            }

            // Events section (remaining screen height)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Eventos da Semana",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    LazyColumn {
                        items(events) { event ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .background(Color.LightGray, MaterialTheme.shapes.medium)
                                    .clickable {
                                        selectedDate = event.startDateTime.toLocalDate()
                                        eventTitle = event.title
                                        eventDescription = event.description
                                        eventLocation = event.location ?: ""
                                        isAllDay = event.isAllDay
                                        isRecurring = event.isRecurring
                                        recurrenceRule = event.recurrenceRule ?: ""
                                        showAddEventDialog = true
                                    }
                            ) {
                                Text(text = event.title, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(8.dp))
                                Text(text = event.startDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 8.dp))
                                Button(
                                    onClick = {
                                        scope.launch {
                                            eventRepository.deleteEvent(event)
                                        }
                                    },
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Text("Deletar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddEventDialog) {
        BasicAlertDialog(
            onDismissRequest = { showAddEventDialog = false },
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.White, shape = MaterialTheme.shapes.medium)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        OutlinedTextField(
                            value = eventTitle,
                            onValueChange = { eventTitle = it },
                            label = { Text("Título") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = eventDescription,
                            onValueChange = { eventDescription = it },
                            label = { Text("Descrição") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = eventLocation,
                            onValueChange = { eventLocation = it },
                            label = { Text("Localização") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = eventStartDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            onValueChange = {
                                eventStartDateTime = LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                            },
                            label = { Text("Data e Hora Início") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = eventEndDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            onValueChange = {
                                eventEndDateTime = LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                            },
                            label = { Text("Data e Hora Fim") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(onClick = {
                                val event = Event(
                                    id = UUID.randomUUID().mostSignificantBits,
                                    title = eventTitle,
                                    description = eventDescription,
                                    location = eventLocation,
                                    startDateTime = eventStartDateTime,
                                    endDateTime = eventEndDateTime,
                                    isAllDay = isAllDay,
                                    isRecurring = isRecurring,
                                    recurrenceRule = recurrenceRule,
                                )
                                scope.launch {
                                    eventRepository.insertEvent(event)
                                }
                                showAddEventDialog = false
                            }) {
                                Text("Salvar")
                            }

                            Button(onClick = { showAddEventDialog = false }) {
                                Text("Cancelar")
                            }
                        }
                    }
                }
            }
        )
    }

    if (showMeetingDialog) {
        BasicAlertDialog(
            onDismissRequest = { showMeetingDialog = false },
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.White, shape = MaterialTheme.shapes.medium)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = "Detalhes do Evento",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text("Título: $eventTitle", modifier = Modifier.padding(bottom = 8.dp))
                        Text("Descrição: $eventDescription", modifier = Modifier.padding(bottom = 8.dp))
                        Text("Localização: $eventLocation", modifier = Modifier.padding(bottom = 8.dp))
                        Text("Data e Hora: ${eventStartDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}", modifier = Modifier.padding(bottom = 8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(onClick = {
                                scope.launch {
                                    eventRepository.deleteEvent(Event(
                                        id = UUID.randomUUID().mostSignificantBits,
                                        title = eventTitle,
                                        description = eventDescription,
                                        location = eventLocation,
                                        startDateTime = eventStartDateTime,
                                        endDateTime = eventEndDateTime,
                                        isAllDay = isAllDay,
                                        isRecurring = isRecurring,
                                        recurrenceRule = recurrenceRule
                                    ))
                                }
                                showMeetingDialog = false
                            }) {
                                Text("Deletar Evento")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(onClick = { showMeetingDialog = false }) {
                                Text("Fechar")
                            }
                        }
                    }
                }
            }
        )
    }
}

fun getWeeksOfMonth(yearMonth: YearMonth): List<List<LocalDate?>> {
    val startOfMonth = yearMonth.atDay(1)
    val endOfMonth = yearMonth.atEndOfMonth()
    val startOfCalendar = startOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
    val endOfCalendar = endOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))

    val weeks = mutableListOf<List<LocalDate?>>()
    var currentWeek = mutableListOf<LocalDate?>()
    var currentDate = startOfCalendar

    while (currentDate <= endOfCalendar) {
        currentWeek.add(currentDate)
        if (currentDate.dayOfWeek == DayOfWeek.SATURDAY) {
            weeks.add(currentWeek)
            currentWeek = mutableListOf()
        }
        currentDate = currentDate.plusDays(1)
    }

    if (currentWeek.isNotEmpty()) {
        while (currentWeek.size < 7) {
            currentWeek.add(null)
        }
        weeks.add(currentWeek)
    }

    return weeks
}
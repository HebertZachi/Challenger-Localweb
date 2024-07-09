package br.com.fiap.challengerlocalweb.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun editAccountManually(navController: NavController, email: String) {
    var emailState by remember { mutableStateOf(TextFieldValue(email)) }
    var password by remember { mutableStateOf(TextFieldValue("12345teste")) }
    var isPop3 by remember { mutableStateOf(true) }

    // POP3/IMAP settings
    var pop3Server by remember { mutableStateOf(TextFieldValue("pop3.example.com")) }
    var pop3Port by remember { mutableStateOf(TextFieldValue("443")) }
    var imapServer by remember { mutableStateOf(TextFieldValue("imap.example.com")) }
    var imapPort by remember { mutableStateOf(TextFieldValue("342")) }

    // SMTP settings
    var smtpServer by remember { mutableStateOf(TextFieldValue("smtp.example.com")) }
    var smtpPort by remember { mutableStateOf(TextFieldValue("254")) }

    // Validations
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var serverError by remember { mutableStateOf(false) }
    var portError by remember { mutableStateOf(false) }
    var smtpServerError by remember { mutableStateOf(false) }
    var smtpPortError by remember { mutableStateOf(false) }

    val context = LocalContext.current

    fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePort(port: String): Boolean {
        return port.toIntOrNull() != null && port.toInt() in 1..65535
    }

    fun validateFields(): Boolean {
        var valid = true

        emailError = !validateEmail(emailState.text)
        passwordError = password.text.isEmpty()
        serverError = if (isPop3) pop3Server.text.isEmpty() else imapServer.text.isEmpty()
        portError = if (isPop3) !validatePort(pop3Port.text) else !validatePort(imapPort.text)
        smtpServerError = smtpServer.text.isEmpty()
        smtpPortError = !validatePort(smtpPort.text)

        valid = !emailError && !passwordError && !serverError && !portError && !smtpServerError && !smtpPortError
        return valid
    }

    fun testConnection(): Boolean {
        if (validateFields()) {
            // Here you would put the actual logic to test the connection
            return true
        }
        return false
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
                    onClick = { navController.navigate("userProfile") }
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Editar Conta Manualmente",
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = emailState,
                    onValueChange = { emailState = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("E-mail") },
                    isError = emailError
                )
                if (emailError) {
                    Text("E-mail inválido", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Senha") },
                    isError = passwordError
                )
                if (passwordError) {
                    Text("Senha não pode estar vazia", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Tipo de Conta:",
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Start)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Checkbox(
                        checked = isPop3,
                        onCheckedChange = {
                            if (it) isPop3 = true
                        }
                    )
                    Text(text = "POP3", color = Color.White)

                    Spacer(modifier = Modifier.width(8.dp))

                    Checkbox(
                        checked = !isPop3,
                        onCheckedChange = {
                            if (it) isPop3 = false
                        }
                    )
                    Text(text = "IMAP", color = Color.White)
                }

                if (isPop3) {
                    OutlinedTextField(
                        value = pop3Server,
                        onValueChange = { pop3Server = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Servidor POP3") },
                        isError = serverError
                    )
                    if (serverError) {
                        Text("Servidor POP3 não pode estar vazio", color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = pop3Port,
                        onValueChange = { pop3Port = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Porta POP3") },
                        isError = portError
                    )
                    if (portError) {
                        Text("Porta POP3 inválida", color = Color.Red, fontSize = 12.sp)
                    }
                } else {
                    OutlinedTextField(
                        value = imapServer,
                        onValueChange = { imapServer = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Servidor IMAP") },
                        isError = serverError
                    )
                    if (serverError) {
                        Text("Servidor IMAP não pode estar vazio", color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = imapPort,
                        onValueChange = { imapPort = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Porta IMAP") },
                        isError = portError
                    )
                    if (portError) {
                        Text("Porta IMAP inválida", color = Color.Red, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Configurações SMTP:",
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = smtpServer,
                    onValueChange = { smtpServer = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Servidor SMTP") },
                    isError = smtpServerError
                )
                if (smtpServerError) {
                    Text("Servidor SMTP não pode estar vazio", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = smtpPort,
                    onValueChange = { smtpPort = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Porta SMTP") },
                    isError = smtpPortError
                )
                if (smtpPortError) {
                    Text("Porta SMTP inválida", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (testConnection()) {
                            Toast.makeText(context, "Conectado com sucesso", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Falha na conexão. Verifique os campos.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Testar Conexão")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (validateFields()) {
                            // Save account details
                            Toast.makeText(context, "Conta atualizada com sucesso", Toast.LENGTH_SHORT).show()
                            navController.navigate("userProfile")
                        } else {
                            Toast.makeText(context, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Atualizar Conta")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("userProfile") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Cancelar")
                }
            }
        }
    }
}

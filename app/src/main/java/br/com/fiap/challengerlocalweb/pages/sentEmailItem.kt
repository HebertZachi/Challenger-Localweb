package br.com.fiap.challengerlocalweb.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.challengerlocalweb.relations.SentEmailWithUsers
import java.time.format.DateTimeFormatter

@Composable
fun sentEmailItem(email: SentEmailWithUsers, navController: NavController) {
    Button(
        onClick = { navController.navigate("sentEmailDetail/${email.sentEmail.sentEmailId}") },
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
                    text = email.sentEmail.subject,
                    color = Color.White,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val formattedDate = email.sentEmail.createdAt.format(dateFormatter)
                Text(
                    text = formattedDate,
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            val receivers = email.receivers
            val displayedEmails = receivers.take(2).joinToString(separator = ", ") { it.userEmailId }
            val moreEmailsCount = receivers.size - 2
            val showMoreText = if (moreEmailsCount > 0) "+$moreEmailsCount mais" else ""

            Text(
                text = "Para: $displayedEmails $showMoreText",
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth()
            )

            Text(
                text = email.sentEmail.body,
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

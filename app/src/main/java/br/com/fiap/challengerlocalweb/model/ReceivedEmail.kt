package br.com.fiap.challengerlocalweb.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "received_emails")
data class ReceivedEmail(
    @PrimaryKey val receivedEmailId: String,
    val subject: String,
    val senderEmail: String,
    val body: String,
    val createdAt: LocalDateTime
)

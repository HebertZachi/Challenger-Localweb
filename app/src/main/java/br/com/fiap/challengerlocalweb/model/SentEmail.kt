package br.com.fiap.challengerlocalweb.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "sent_emails")
data class SentEmail(
    @PrimaryKey val sentEmailId: String,
    val subject: String,
    val senderEmail: String,
    val body: String,
    val createdAt: LocalDateTime
)

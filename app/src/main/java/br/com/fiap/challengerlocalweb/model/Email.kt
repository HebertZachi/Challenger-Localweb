package br.com.fiap.challengerlocalweb.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Email(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val recipient: List<String> = emptyList(),
    val carbonCopy: List<String> = emptyList(),
    val subject: String,
    val body: String,
    val date: LocalDateTime,
)

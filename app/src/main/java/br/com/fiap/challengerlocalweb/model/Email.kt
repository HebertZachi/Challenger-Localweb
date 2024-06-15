package br.com.fiap.challengerlocalweb.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

data class Email(
    val sender: String = "",
    val recipient: String = "",
    val cc: List<String> = listOf(),
    val subject: String = "",
    val body: String = "",
)
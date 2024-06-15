package br.com.fiap.challengerlocalweb.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded
import java.time.LocalDateTime

@Entity(tableName = "sent_emails")
data class SentEmail(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @Embedded val baseEmail: Email,
    @ColumnInfo(name = "creation_date")
    val creationDate: LocalDateTime = LocalDateTime.now()
)

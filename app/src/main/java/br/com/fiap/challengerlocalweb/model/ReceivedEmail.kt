package br.com.fiap.challengerlocalweb.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded
import java.time.LocalDateTime

@Entity(tableName = "received_emails")
data class ReceivedEmail(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @Embedded val baseEmail: Email,
    val sender: String = "",
    @ColumnInfo(name = "received_date")
    val receivedDate: LocalDateTime = LocalDateTime.now()
)

package br.com.fiap.challengerlocalweb.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "recipient",
//    foreignKeys = [ForeignKey(
//        entity = Email::class,
//        parentColumns = ["emailId"],
//        childColumns = ["emailId"],
//        onDelete = ForeignKey.CASCADE
//    )]
)
data class Recipient(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val recipientId: Long = 0,
    val email: String,
)
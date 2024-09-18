package br.com.fiap.challengerlocalweb.model
import androidx.room.Entity

@Entity(primaryKeys = ["sentEmailId", "userEmailId"])
data class SentEmailReceiverCrossRef(
    val sentEmailId: String,
    val userEmailId: String
)

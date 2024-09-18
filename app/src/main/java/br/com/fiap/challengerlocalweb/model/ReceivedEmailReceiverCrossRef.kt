package br.com.fiap.challengerlocalweb.model

import androidx.room.Entity

@Entity(primaryKeys = ["receivedEmailId", "userEmailId"])
data class ReceivedEmailReceiverCrossRef(
    val receivedEmailId: String,
    val userEmailId: String
)

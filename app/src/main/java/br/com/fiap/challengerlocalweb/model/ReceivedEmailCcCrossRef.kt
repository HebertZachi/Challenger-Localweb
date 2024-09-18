package br.com.fiap.challengerlocalweb.model

import androidx.room.Entity

@Entity(primaryKeys = ["receivedEmailId", "userEmailId"])
data class ReceivedEmailCcCrossRef(
    val receivedEmailId: String,
    val userEmailId: String
)

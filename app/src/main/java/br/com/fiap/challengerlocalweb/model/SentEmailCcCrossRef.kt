package br.com.fiap.challengerlocalweb.model

import androidx.room.Entity

@Entity(primaryKeys = ["sentEmailId", "userEmailId"])
data class SentEmailCcCrossRef(
    val sentEmailId: String,
    val userEmailId: String
)

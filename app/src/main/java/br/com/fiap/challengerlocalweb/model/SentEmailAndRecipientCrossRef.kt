package br.com.fiap.challengerlocalweb.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["sent_email_id", "recipient_id"])
data class SentEmailAndRecipientCrossRef(
    @ColumnInfo(name = "sent_email_id") val sentEmailId: Long,
    @ColumnInfo(name = "recipient_id") val recipientId: Long
)
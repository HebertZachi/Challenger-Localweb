package br.com.fiap.challengerlocalweb.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["received_email_id", "recipient_id"])
data class ReceivedEmailAndRecipientCrossRef(
    @ColumnInfo(name = "received_email_id") val receivedEmailId: Long,
    @ColumnInfo(name = "recipient_id") val recipientId: Long
)
package br.com.fiap.challengerlocalweb.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["received_email_id", "cc_id"])
data class ReceivedEmailAndCCCrossRef(
    @ColumnInfo(name = "received_email_id") val receivedEmailId: Long,
    @ColumnInfo(name = "cc_id") val ccId: Long
)

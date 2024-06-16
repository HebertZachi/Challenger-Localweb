package br.com.fiap.challengerlocalweb.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["sent_email_id", "cc_id"])
data class SentEmailAndCCCrossRef(
    @ColumnInfo(name = "sent_email_id") val sentEmailId: Long,
    @ColumnInfo(name = "cc_id") val ccId: Long
)

package br.com.fiap.challengerlocalweb.model.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import br.com.fiap.challengerlocalweb.model.Recipient
import br.com.fiap.challengerlocalweb.model.ReceivedEmail
import br.com.fiap.challengerlocalweb.model.SentEmailAndRecipientCrossRef

data class SentEmailWithRecipient(
    @Embedded val receivedEmail: ReceivedEmail,
    @Relation(
        parentColumn = "id",
        entityColumn = "sent_email_id",
        associateBy = Junction(SentEmailAndRecipientCrossRef::class)
    )
    val recipients: List<Recipient>
)

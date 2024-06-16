package br.com.fiap.challengerlocalweb.model.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import br.com.fiap.challengerlocalweb.model.Recipient
import br.com.fiap.challengerlocalweb.model.ReceivedEmail
import br.com.fiap.challengerlocalweb.model.ReceivedEmailAndRecipientCrossRef

data class ReceivedEmailWithRecipient(
    @Embedded val receivedEmail: ReceivedEmail,
    @Relation(
        parentColumn = "id",
        entityColumn = "received_email_id",
        associateBy = Junction(ReceivedEmailAndRecipientCrossRef::class)
    )
    val recipients: List<Recipient>
)

package br.com.fiap.challengerlocalweb.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import br.com.fiap.challengerlocalweb.model.ReceivedEmail
import br.com.fiap.challengerlocalweb.model.User
import br.com.fiap.challengerlocalweb.model.ReceivedEmailCcCrossRef
import br.com.fiap.challengerlocalweb.model.ReceivedEmailReceiverCrossRef

data class ReceivedEmailWithUsers(
    @Embedded val receivedEmail: ReceivedEmail,
    @Relation(
        parentColumn = "receivedEmailId",
        entityColumn = "userEmailId",
        associateBy = Junction(ReceivedEmailReceiverCrossRef::class)
    )
    val receivers: List<User>,
    @Relation(
        parentColumn = "receivedEmailId",
        entityColumn = "userEmailId",
        associateBy = Junction(ReceivedEmailCcCrossRef::class)
    )
    val cc: List<User>
)

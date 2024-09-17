package br.com.fiap.challengerlocalweb.relations

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Junction
import androidx.room.Relation
import br.com.fiap.challengerlocalweb.model.SentEmail
import br.com.fiap.challengerlocalweb.model.SentEmailCcCrossRef
import br.com.fiap.challengerlocalweb.model.SentEmailReceiverCrossRef
import br.com.fiap.challengerlocalweb.model.User

data class SentEmailWithUsers(
    @Embedded val sentEmail: SentEmail,
    @Relation(
        parentColumn = "sentEmailId",
        entityColumn = "userEmailId",
        associateBy = Junction(SentEmailReceiverCrossRef::class)
    )
    val receivers: List<User>,
    @Relation(
        parentColumn = "sentEmailId",
        entityColumn = "userEmailId",
        associateBy = Junction(SentEmailCcCrossRef::class)
    )
    val cc: List<User>
)


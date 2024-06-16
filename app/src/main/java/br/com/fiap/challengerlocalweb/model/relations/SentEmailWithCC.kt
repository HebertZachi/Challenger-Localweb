package br.com.fiap.challengerlocalweb.model.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import br.com.fiap.challengerlocalweb.model.CC
import br.com.fiap.challengerlocalweb.model.SentEmail
import br.com.fiap.challengerlocalweb.model.SentEmailAndCCCrossRef

data class SentEmailWithCC(
    @Embedded val sentEmail: SentEmail,
    @Relation(
        parentColumn = "id",
        entityColumn = "sent_email_id",
        associateBy = Junction(SentEmailAndCCCrossRef::class)
    )
    val ccs: List<CC>
)

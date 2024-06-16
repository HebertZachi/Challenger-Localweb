package br.com.fiap.challengerlocalweb.model.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import br.com.fiap.challengerlocalweb.model.CC
import br.com.fiap.challengerlocalweb.model.ReceivedEmail
import br.com.fiap.challengerlocalweb.model.ReceivedEmailAndCCCrossRef

data class ReceivedEmailWithCC(
    @Embedded val receivedEmail: ReceivedEmail,
    @Relation(
        parentColumn = "id",
        entityColumn = "cc_id",
        associateBy = Junction(ReceivedEmailAndCCCrossRef::class)
    )
    val ccs: List<CC>
)

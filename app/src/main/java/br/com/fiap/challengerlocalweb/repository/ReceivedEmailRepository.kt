package br.com.fiap.challengerlocalweb.repository

import android.content.Context
import br.com.fiap.challengerlocalweb.AppDatabase
import br.com.fiap.challengerlocalweb.model.ReceivedEmail
import br.com.fiap.challengerlocalweb.model.ReceivedEmailCcCrossRef
import br.com.fiap.challengerlocalweb.model.ReceivedEmailReceiverCrossRef
import br.com.fiap.challengerlocalweb.relations.ReceivedEmailWithUsers

class ReceivedEmailRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context).receivedEmailDao()

    suspend fun insertEmail(email: ReceivedEmail, receivers: List<String>, cc: List<String>) {
        db.insertEmail(email)

        val receiverCrossRefs = receivers.map { recipientEmail ->
            ReceivedEmailReceiverCrossRef(receivedEmailId = email.receivedEmailId, userEmailId = recipientEmail)
        }
        db.insertEmailReceiversCrossRef(receiverCrossRefs)

        val ccCrossRefs = cc.map { recipientEmail ->
            ReceivedEmailCcCrossRef(receivedEmailId = email.receivedEmailId, userEmailId = recipientEmail)
        }
        db.insertEmailCcCrossRef(ccCrossRefs)
    }

    suspend fun getAllEmails(): List<ReceivedEmailWithUsers> {
        return db.getAllEmails()
    }

    suspend fun getEmailById(id: String): ReceivedEmailWithUsers? {
        return db.getEmailById(id)
    }

    suspend fun updateEmail(email: ReceivedEmail, receivers: List<String>, cc: List<String>) {
        db.updateEmail(email)
        db.deleteEmailReceiversCrossRefs(email.receivedEmailId)
        db.deleteEmailCcCrossRefs(email.receivedEmailId)

        val receiverCrossRefs = receivers.map { recipientEmail ->
            ReceivedEmailReceiverCrossRef(receivedEmailId = email.receivedEmailId, userEmailId = recipientEmail)
        }
        db.insertEmailReceiversCrossRef(receiverCrossRefs)

        val ccCrossRefs = cc.map { recipientEmail ->
            ReceivedEmailCcCrossRef(receivedEmailId = email.receivedEmailId, userEmailId = recipientEmail)
        }
        db.insertEmailCcCrossRef(ccCrossRefs)
    }

    suspend fun deleteEmail(email: ReceivedEmail) {
        db.deleteEmailReceiversCrossRefs(email.receivedEmailId)
        db.deleteEmailCcCrossRefs(email.receivedEmailId)

        db.deleteEmail(email)
    }
}

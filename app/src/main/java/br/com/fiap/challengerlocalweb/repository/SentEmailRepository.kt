package br.com.fiap.challengerlocalweb.repository

import android.content.Context
import android.util.Log
import br.com.fiap.challengerlocalweb.AppDatabase
import br.com.fiap.challengerlocalweb.model.SentEmail
import br.com.fiap.challengerlocalweb.model.SentEmailCcCrossRef
import br.com.fiap.challengerlocalweb.model.SentEmailReceiverCrossRef
import br.com.fiap.challengerlocalweb.relations.SentEmailWithUsers
import kotlin.math.log

class SentEmailRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context).sentEmailDao()

    suspend fun insertSentEmail(email: SentEmail, receivers: List<String>, cc: List<String>) {
        db.insertSentEmail(email)

        val receiverCrossRefs = receivers.map { recipientEmail ->
            SentEmailReceiverCrossRef(sentEmailId = email.sentEmailId, userEmailId = recipientEmail)
        }
        db.insertSentEmailReceiversCrossRef(receiverCrossRefs)

        val ccCrossRefs = cc.map { recipientEmail ->
            SentEmailCcCrossRef(sentEmailId = email.sentEmailId, userEmailId = recipientEmail)
        }
        db.insertSentEmailCcCrossRef(ccCrossRefs)
    }

    suspend fun getAllSentEmails(): List<SentEmailWithUsers> {
        val result = db.getAllSentEmails()
        Log.d("AlgumaCoisa", "getAllSentEmails: $result")
        return result
    }

//    suspend fun getAllSentEmails(): List<SentEmailWithUsers> {
//        val result = db.getAllSentEmails()
//        Log.d("AlgumaCoisa", "getAllSentEmails: $result")
//        return result
//    }



    suspend fun getSentEmailById(id: String): SentEmailWithUsers? {
        return db.getSentEmailById(id)
    }

    suspend fun updateSentEmail(email: SentEmail, receivers: List<String>, cc: List<String>) {
        db.updateSentEmail(email)
        db.deleteSentEmailReceiversCrossRefs(email.sentEmailId)
        db.deleteSentEmailCcCrossRefs(email.sentEmailId)

        val receiverCrossRefs = receivers.map { recipientEmail ->
            SentEmailReceiverCrossRef(sentEmailId = email.sentEmailId, userEmailId = recipientEmail)
        }
        db.insertSentEmailReceiversCrossRef(receiverCrossRefs)

        val ccCrossRefs = cc.map { recipientEmail ->
            SentEmailCcCrossRef(sentEmailId = email.sentEmailId, userEmailId = recipientEmail)
        }
        db.insertSentEmailCcCrossRef(ccCrossRefs)
    }

    suspend fun deleteSentEmail(email: SentEmail) {
        db.deleteSentEmailReceiversCrossRefs(email.sentEmailId)
        db.deleteSentEmailCcCrossRefs(email.sentEmailId)
        db.deleteSentEmail(email)
    }
}

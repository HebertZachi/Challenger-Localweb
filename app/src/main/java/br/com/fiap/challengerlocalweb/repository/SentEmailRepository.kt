package br.com.fiap.challengerlocalweb.repository

import android.content.Context
import android.util.Log
import br.com.fiap.challengerlocalweb.AppDatabase
import br.com.fiap.challengerlocalweb.model.SentEmail
import br.com.fiap.challengerlocalweb.model.SentEmailCcCrossRef
import br.com.fiap.challengerlocalweb.model.SentEmailReceiverCrossRef
import br.com.fiap.challengerlocalweb.model.User
import br.com.fiap.challengerlocalweb.relations.SentEmailWithUsers

class SentEmailRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context)
    private val sentEmailDao = db.sentEmailDao()
    private val userDao = db.userDao()

    suspend fun insertSentEmail(email: SentEmail, receivers: List<String>, cc: List<String>) {
        sentEmailDao.insertSentEmail(email)

        receivers.forEach { recipientEmail ->
            val userExists = userDao.userExists(recipientEmail) > 0
            if (!userExists) {
                userDao.insertUser(User(userEmailId = recipientEmail, name = null))
            }
        }

        val receiverCrossRefs = receivers.map { recipientEmail ->
            SentEmailReceiverCrossRef(sentEmailId = email.sentEmailId, userEmailId = recipientEmail)
        }
        sentEmailDao.insertSentEmailReceiversCrossRef(receiverCrossRefs)

        cc.forEach { recipientEmail ->
            val userExists = userDao.userExists(recipientEmail) > 0
            if (!userExists) {
                userDao.insertUser(User(userEmailId = recipientEmail, name = null))
            }
        }

        val ccCrossRefs = cc.map { recipientEmail ->
            SentEmailCcCrossRef(sentEmailId = email.sentEmailId, userEmailId = recipientEmail)
        }
        sentEmailDao.insertSentEmailCcCrossRef(ccCrossRefs)
    }

    suspend fun getAllSentEmails(): List<SentEmailWithUsers> {
        return sentEmailDao.getAllSentEmails()
    }

    suspend fun getSentEmailById(id: String): SentEmailWithUsers? {
        return sentEmailDao.getSentEmailById(id)
    }

    suspend fun updateSentEmail(email: SentEmail, receivers: List<String>, cc: List<String>) {
        sentEmailDao.updateSentEmail(email)
        sentEmailDao.deleteSentEmailReceiversCrossRefs(email.sentEmailId)
        sentEmailDao.deleteSentEmailCcCrossRefs(email.sentEmailId)

        val receiverCrossRefs = receivers.map { recipientEmail ->
            SentEmailReceiverCrossRef(sentEmailId = email.sentEmailId, userEmailId = recipientEmail)
        }
        sentEmailDao.insertSentEmailReceiversCrossRef(receiverCrossRefs)

        val ccCrossRefs = cc.map { recipientEmail ->
            SentEmailCcCrossRef(sentEmailId = email.sentEmailId, userEmailId = recipientEmail)
        }
        sentEmailDao.insertSentEmailCcCrossRef(ccCrossRefs)
    }

    suspend fun deleteSentEmail(email: SentEmail) {
        sentEmailDao.deleteSentEmailReceiversCrossRefs(email.sentEmailId)
        sentEmailDao.deleteSentEmailCcCrossRefs(email.sentEmailId)
        sentEmailDao.deleteSentEmail(email)
    }
}

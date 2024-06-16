package br.com.fiap.challengerlocalweb.repository

import android.content.Context
import br.com.fiap.challengerlocalweb.AppDatabase
import br.com.fiap.challengerlocalweb.model.ReceivedEmail
import br.com.fiap.challengerlocalweb.model.relations.ReceivedEmailWithCC
import br.com.fiap.challengerlocalweb.model.relations.ReceivedEmailWithRecipient

class ReceivedEmailRepository(context: Context) {

    private val db = AppDatabase.getDataBase(context).receivedEmailDao()

    suspend fun save(email: ReceivedEmail): Long {
        return db.save(email)
    }

    suspend fun update(email: ReceivedEmail): Int {
        return db.update(email)
    }

    suspend fun delete(email: ReceivedEmail): Int {
        return db.delete(email)
    }

    suspend fun findById(id: Long): ReceivedEmail {
        return db.findById(id)
    }

    suspend fun findAll(): List<ReceivedEmail> {
        return db.findAll()
    }

    suspend fun getReceivedEmailWithCC(id: Long): List<ReceivedEmailWithCC> {
        return db.getReceivedEmailWithCC(id)
    }

    suspend fun getReceivedEmailWithRecipient(id: Long): List<ReceivedEmailWithRecipient> {
        return db.getReceivedEmailWithRecipient(id)
    }
}

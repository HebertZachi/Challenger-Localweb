package br.com.fiap.challengerlocalweb.repository

import android.content.Context
import br.com.fiap.challengerlocalweb.AppDatabase
import br.com.fiap.challengerlocalweb.model.SentEmail
import br.com.fiap.challengerlocalweb.model.relations.SentEmailWithCC
import br.com.fiap.challengerlocalweb.model.relations.SentEmailWithRecipient

class SentEmailRepository(context: Context) {

    private val db = AppDatabase.getDataBase(context).sentEmailDao()

    suspend fun save(email: SentEmail): Long {
        return db.save(email)
    }

    suspend fun update(email: SentEmail): Int {
        return db.update(email)
    }

    suspend fun delete(email: SentEmail): Int {
        return db.delete(email)
    }

    suspend fun findById(id: Long): SentEmail {
        return db.findById(id)
    }

    suspend fun findAll(): List<SentEmail> {
        return db.findAll()
    }

    suspend fun getSentEmailWithCC(id: Long): List<SentEmailWithCC> {
        return db.getSentEmailWithCC(id)
    }

    suspend fun getSentEmailWithRecipient(id: Long): List<SentEmailWithRecipient> {
        return db.getSentEmailWithRecipient(id)
    }
}

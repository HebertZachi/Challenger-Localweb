package br.com.fiap.challengerlocalweb.repository

import android.content.Context
import br.com.fiap.challengerlocalweb.AppDatabase
import br.com.fiap.challengerlocalweb.model.SentEmail

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
}


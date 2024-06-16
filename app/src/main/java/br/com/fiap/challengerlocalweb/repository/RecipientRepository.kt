package br.com.fiap.challengerlocalweb.repository

import android.content.Context
import br.com.fiap.challengerlocalweb.AppDatabase
import br.com.fiap.challengerlocalweb.model.Recipient

class RecipientRepository(context: Context) {

    private val db = AppDatabase.getDataBase(context).recipientDao()

    suspend fun save(recipient: Recipient): Long {
        return db.save(recipient)
    }

    suspend fun update(recipient: Recipient): Int {
        return db.update(recipient)
    }

    suspend fun delete(recipient: Recipient): Int {
        return db.delete(recipient)
    }

    suspend fun findById(id: Long): Recipient {
        return db.findById(id)
    }

    suspend fun findAll(): List<Recipient> {
        return db.findAll()
    }
}

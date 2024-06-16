package br.com.fiap.challengerlocalweb.repository

import android.content.Context
import br.com.fiap.challengerlocalweb.AppDatabase
import br.com.fiap.challengerlocalweb.model.CC

class CCRepository(context: Context) {

    private val db = AppDatabase.getDataBase(context).ccDao()

    suspend fun save(cc: CC): Long {
        return db.save(cc)
    }

    suspend fun update(cc: CC): Int {
        return db.update(cc)
    }

    suspend fun delete(cc: CC): Int {
        return db.delete(cc)
    }

    suspend fun findById(id: Long): CC {
        return db.findById(id)
    }

    suspend fun findAll(): List<CC> {
        return db.findAll()
    }
}

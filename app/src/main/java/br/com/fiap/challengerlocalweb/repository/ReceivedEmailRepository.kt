package br.com.fiap.challengerlocalweb.repository

import android.content.Context
import br.com.fiap.challengerlocalweb.dao.Database
import br.com.fiap.challengerlocalweb.model.ReceivedEmail

class ReceivedEmailRepository(context: Context) {

    private val db = Database.getDataBase(context).receivedEmailDao()

    fun save(email: ReceivedEmail): Long {
        return db.save(email)
    }
}
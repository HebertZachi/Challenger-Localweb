package br.com.fiap.challengerlocalweb.repository

import android.content.Context
import br.com.fiap.challengerlocalweb.AppDatabase
import br.com.fiap.challengerlocalweb.model.Event
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class EventRepository(context: Context) {

    private val db = AppDatabase.getDataBase(context).eventDao()

    suspend fun insertEvent(event: Event): Long {
        return db.insertEvent(event)
    }

    suspend fun updateEvent(event: Event): Int {
        return db.updateEvent(event)
    }

    suspend fun deleteEvent(event: Event): Int {
        return db.deleteEvent(event)
    }

    suspend fun getEventById(eventId: String): Event? {
        return db.getEventById(eventId)
    }

    suspend fun getAllEvents(): Flow<kotlin.collections.List<Event>>

    {
        return db.getAllEvents()
    }

    fun getEventsBetween(start: LocalDateTime, end: LocalDateTime): Flow<kotlin.collections.List<Event>> {
        return db.getEventsBetween(start, end)
    }

    suspend fun deleteAllEvents() {
        return db.deleteAllEvents()
    }
}

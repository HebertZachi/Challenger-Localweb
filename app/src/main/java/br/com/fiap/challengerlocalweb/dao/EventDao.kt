package br.com.fiap.challengerlocalweb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.challengerlocalweb.model.Event
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event): Long

    @Update
    suspend fun updateEvent(event: Event): Int

    @Delete
    suspend fun deleteEvent(event: Event): Int

    @Query("SELECT * FROM events WHERE id = :eventId")
    suspend fun getEventById(eventId: String): Event?

    @Query("SELECT * FROM events ORDER BY startDateTime ASC")
    fun getAllEvents(): Flow<List<Event>>

    @Query("SELECT * FROM events WHERE startDateTime BETWEEN :start AND :end ORDER BY startDateTime ASC")
    fun getEventsBetween(start: LocalDateTime, end: LocalDateTime): Flow<List<Event>>

    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()
}

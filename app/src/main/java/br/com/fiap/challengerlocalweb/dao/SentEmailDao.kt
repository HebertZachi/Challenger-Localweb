package br.com.fiap.challengerlocalweb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.challengerlocalweb.model.SentEmail

@Dao
interface SentEmailDao {
    @Insert
    suspend fun save(email: SentEmail): Long
    @Update
    suspend fun update(email: SentEmail): Long
    @Delete
    suspend fun delete(email: SentEmail): Long

    @Query("SELECT * FROM sent_emails WHERE id = :id")
    suspend fun findById(id: Long): Long

    @Query("SELECT * FROM sent_emails ORDER BY creation_date ASC")
    suspend fun findAll(email: SentEmail): List<SentEmail>


}
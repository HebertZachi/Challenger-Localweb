package br.com.fiap.challengerlocalweb.dao

import androidx.room.*
import br.com.fiap.challengerlocalweb.model.SentEmail

@Dao
interface SentEmailDao {
    @Insert
    suspend fun save(email: SentEmail): Long

    @Update
    suspend fun update(email: SentEmail): Int

    @Delete
    suspend fun delete(email: SentEmail): Int

    @Query("SELECT * FROM sent_emails WHERE id = :id")
    suspend fun findById(id: Long): SentEmail

    @Query("SELECT * FROM sent_emails ORDER BY creation_date ASC")
    suspend fun findAll(): List<SentEmail>
}

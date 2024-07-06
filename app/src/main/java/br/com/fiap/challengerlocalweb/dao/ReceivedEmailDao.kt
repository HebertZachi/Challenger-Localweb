package br.com.fiap.challengerlocalweb.dao

import androidx.room.*
import br.com.fiap.challengerlocalweb.model.ReceivedEmail

@Dao
interface ReceivedEmailDao {
    @Insert
    suspend fun save(email: ReceivedEmail): Long

    @Update
    suspend fun update(email: ReceivedEmail): Int

    @Delete
    suspend fun delete(email: ReceivedEmail): Int

    @Query("SELECT * FROM received_emails WHERE id = :id")
    suspend fun findById(id: Long): ReceivedEmail

    @Query("SELECT * FROM received_emails ORDER BY received_date ASC")
    suspend fun findAll(): List<ReceivedEmail>
}

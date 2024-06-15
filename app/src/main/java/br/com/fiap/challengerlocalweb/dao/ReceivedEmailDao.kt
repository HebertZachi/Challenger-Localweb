package br.com.fiap.challengerlocalweb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.challengerlocalweb.model.ReceivedEmail

@Dao
interface ReceivedEmailDao {
    @Insert
    fun save(email: ReceivedEmail): Long
    @Update
    fun update(email: ReceivedEmail): Int
    @Delete
    fun delete(email: ReceivedEmail): Int

    @Query("SELECT * FROM received_emails WHERE id = :id")
    fun findById(id: Long): ReceivedEmail

    @Query("SELECT * FROM received_emails ORDER BY received_date ASC")
    fun findAll(): List<ReceivedEmail>
}
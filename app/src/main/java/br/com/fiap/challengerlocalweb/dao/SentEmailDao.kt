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
    fun save(email: SentEmail): Long
    @Update
    fun update(email: SentEmail): Int
    @Delete
    fun delete(email: SentEmail): Int

    @Query("SELECT * FROM sent_emails WHERE id = :id")
    fun findById(id: Long): SentEmail

    @Query("SELECT * FROM sent_emails ORDER BY creation_date ASC")
    fun findAll(): List<SentEmail>


}
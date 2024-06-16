package br.com.fiap.challengerlocalweb.dao

import androidx.room.*
import br.com.fiap.challengerlocalweb.model.Recipient

@Dao
interface RecipientDao {
    @Insert
    suspend fun save(recipient: Recipient): Long

    @Update
    suspend fun update(recipient: Recipient): Int

    @Delete
    suspend fun delete(recipient: Recipient): Int

    @Query("SELECT * FROM recipient WHERE id = :id")
    suspend fun findById(id: Long): Recipient

    @Query("SELECT * FROM recipient ORDER BY email ASC")
    suspend fun findAll(): List<Recipient>
}

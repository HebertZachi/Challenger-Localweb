package br.com.fiap.challengerlocalweb.dao

import androidx.room.*
import br.com.fiap.challengerlocalweb.model.CC

@Dao
interface CCDao {
    @Insert
    suspend fun save(cc: CC): Long

    @Update
    suspend fun update(cc: CC): Int

    @Delete
    suspend fun delete(cc: CC): Int

    @Query("SELECT * FROM carbon_copy WHERE id = :id")
    suspend fun findById(id: Long): CC

    @Query("SELECT * FROM carbon_copy ORDER BY email ASC")
    suspend fun findAll(): List<CC>
}

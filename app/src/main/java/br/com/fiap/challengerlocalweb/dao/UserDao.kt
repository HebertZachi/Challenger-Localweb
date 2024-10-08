package br.com.fiap.challengerlocalweb.dao

import androidx.room.*
import br.com.fiap.challengerlocalweb.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE userEmailId = :userId")
    suspend fun getUserById(userId: String): User?

    @Query("SELECT * FROM users WHERE userEmailId = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT COUNT(*) FROM users WHERE userEmailId = :userEmailId")
    suspend fun userExists(userEmailId: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)
}


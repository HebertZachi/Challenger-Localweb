package br.com.fiap.challengerlocalweb.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val userEmailId: String,
    val name: String?,
)

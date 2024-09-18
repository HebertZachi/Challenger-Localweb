package br.com.fiap.wavemail.model

import java.util.UUID

data class User(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val email: String,
    val password: String,
    val Token: String
)

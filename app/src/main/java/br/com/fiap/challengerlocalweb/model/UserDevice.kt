package br.com.fiap.challengerlocalweb.model

import java.util.UUID

data class UserDevice(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val email: String,
    val password: String,
    val Token: String
)


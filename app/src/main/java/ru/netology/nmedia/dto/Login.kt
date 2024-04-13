package ru.netology.nmedia.dto

data class Login(
    val username: String = "",
    val password: String = "",
)

data class AuthResult(
    val success: Boolean
)

package ru.netology.nmedia.dto

data class Login(
    val username: String = "",
    val password: String = "",
    val error: Boolean = false,
)

package com.project.calowry_app.database.auth

data class AuthBody(
    val name: String,
    val email: String,
    val password: String,
    val confirmPassword: String = password
)
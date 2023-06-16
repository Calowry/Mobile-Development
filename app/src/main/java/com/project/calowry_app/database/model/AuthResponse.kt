package com.project.calowry_app.database.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("error")
    val error: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val loginResult: User
)
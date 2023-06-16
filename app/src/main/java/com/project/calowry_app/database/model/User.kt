package com.project.calowry_app.database.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("roleId")
    val roleId: Int,
    @SerializedName("verified")
    val verified: String,
    @SerializedName("active")
    val active: String,
    @SerializedName("token")
    val token: String
)
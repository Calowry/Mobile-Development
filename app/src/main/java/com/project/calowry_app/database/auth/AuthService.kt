package com.project.calowry_app.database.auth

import com.project.calowry_app.database.model.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("/user/signup")
    suspend fun registerUser(
        @Body authBody: AuthBody
    ): Response<AuthResponse>

    @POST("/user/login ")
    suspend fun loginUser(
        @Body loginBody: LoginBody
    ): AuthResponse

}
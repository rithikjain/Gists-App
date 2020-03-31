package com.rithikjain.projectgists.network

import com.rithikjain.projectgists.model.register.RegisterRequest
import com.rithikjain.projectgists.model.register.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiInterface {

    @POST("user/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

}
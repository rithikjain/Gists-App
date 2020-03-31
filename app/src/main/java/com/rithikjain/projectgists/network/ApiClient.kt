package com.rithikjain.projectgists.network

import com.rithikjain.projectgists.model.register.RegisterRequest

class ApiClient(private val api: ApiInterface) : BaseApiClient() {

    suspend fun registerUser(registerRequest: RegisterRequest) =
        getResult { api.registerUser(registerRequest) }

}
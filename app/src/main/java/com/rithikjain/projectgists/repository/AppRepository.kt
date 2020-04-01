package com.rithikjain.projectgists.repository

import com.rithikjain.projectgists.db.AppDao
import com.rithikjain.projectgists.model.gists.CreateGistRequest
import com.rithikjain.projectgists.model.gists.DeleteGistRequest
import com.rithikjain.projectgists.model.register.RegisterRequest
import com.rithikjain.projectgists.network.ApiClient

class AppRepository(private val apiClient: ApiClient, private val appDao: AppDao) : BaseRepo() {

    fun registerUser(registerRequest: RegisterRequest) =
        makeRequest { apiClient.registerUser(registerRequest) }

    fun viewAllGists() = makeRequest { apiClient.viewAllGists() }

    fun createGist(createGistRequest: CreateGistRequest) =
        makeRequest { apiClient.createGist(createGistRequest) }

    fun deleteGist(deleteGistRequest: DeleteGistRequest) =
        makeRequest { apiClient.deleteGist(deleteGistRequest) }

}
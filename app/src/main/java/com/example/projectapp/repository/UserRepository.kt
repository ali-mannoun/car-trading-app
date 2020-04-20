package com.example.projectapp.repository

import android.util.Log
import com.example.projectapp.domain.User
import com.example.projectapp.network.*

/**
 * Repository provides an interface to fetch a title or request a new one be generated.
 *
 * Repository modules handle data operations. They provide a clean API so that the rest of the app
 * can retrieve this data easily. They know where to get the data from and what API calls to make
 * when data is updated. You can consider repositories to be mediators between different data
 * sources, in our case it mediates between a network API and an offline database cache.
 */
class UserRepository(private val webService: IApiService) {

    suspend fun checkCredentials(email: String, password: String):Int {
        val credentials = LoginProperty(email = email, password = password)
        val checkResponse = webService.checkUserCredentials(user = credentials)
        return checkResponse.code()
    }

    suspend fun login(email: String, password: String): User? {

        val loginInfo = LoginProperty(email = email, password = password)
        //Check if the user exists in the database. if the user exists, then we login the user to the system.
        // else we return null
        val checkResponse = webService.checkUserCredentials(user = loginInfo)
        if (checkResponse.isSuccessful) {
            //The user exists in the DB, then we login the user and return the user info.
            val response = webService.login(user = loginInfo)
            if (response.isSuccessful) {
                return response.body()?.asUserDomainModel()
            }
        }
        return null
    }

    suspend fun createNewAccount(name: String, email: String, password: String): User? {
        val loginInfo = LoginProperty(email = email, password = password)
        val checkResponse = webService.checkUserCredentials(user = loginInfo)
        when (checkResponse.code()) {
            404 -> {
                //the entered credentials don't exist in the db ,and we can proceed register process
                val userInfo = RegisterProperty(name = name, email = email, password = password)
                val response = webService.createNewAccount(user = userInfo)
                if (response.isSuccessful) {
                    return webService.login(user = loginInfo).body()?.asUserDomainModel()
                }
            }
        }
        return null
    }

    //TODO store uesr class when remember me is checked so we can fetch that user to show in profile fragment

    /**
     * Thrown when there was a error fetching a user
     *
     * @property message user ready error message
     * @property cause the original cause of this exception
     */
    class UserFetchingError(message: String, cause: Throwable) : Exception(message, cause)

}
package com.example.projectapp.repository

import com.example.projectapp.network.IApiService
import com.example.projectapp.network.LoginProperty
import com.example.projectapp.network.RegisterProperty
import com.example.projectapp.network.UserProperty

/**
 * Repository provides an interface to fetch a title or request a new one be generated.
 *
 * Repository modules handle data operations. They provide a clean API so that the rest of the app
 * can retrieve this data easily. They know where to get the data from and what API calls to make
 * when data is updated. You can consider repositories to be mediators between different data
 * sources, in our case it mediates between a network API and an offline database cache.
 */
class UserRepository(private val webService: IApiService) {

    suspend fun login(email: String, password: String): UserProperty? {
        val loginInfo = LoginProperty(email = email, password = password)

        val checkResponse = webService.checkUserCredentials(user = loginInfo)
        if (checkResponse.isSuccessful) {
            val response = webService.login(user = loginInfo)
            if (response.isSuccessful) {
                return response.body()
            }
        }
        return null
    }

    suspend fun createNewAccount(name: String, email: String, password: String): UserProperty? {
        val loginInfo = LoginProperty(email = email, password = password)
        val checkResponse = webService.checkUserCredentials(user = loginInfo)
        when (checkResponse.code()) {
            404 -> {
                //the entered credentials don't exist in the db ,and we can proceed register process
                val userInfo = RegisterProperty(name = name, email = email, password = password)
                val response = webService.createNewAccount(user = userInfo)
                if (response.isSuccessful) {
                    return webService.login(user = loginInfo).body()
                }
            }
        }
        return null
    }

    /**
     * Thrown when there was a error fetching a user
     *
     * @property message user ready error message
     * @property cause the original cause of this exception
     */
    class UserFetchingError(message: String, cause: Throwable) : Throwable(message, cause)

}
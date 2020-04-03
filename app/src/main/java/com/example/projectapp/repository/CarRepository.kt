package com.example.projectapp.repository

import androidx.lifecycle.LiveData
import com.example.projectapp.database.CarsDatabaseDao
import com.example.projectapp.network.CarProperty
import com.example.projectapp.network.CarSpecificationsProperty
import com.example.projectapp.network.IApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

/**
 */
/**
 * Repository for fetching cars from the network and storing them on disk
 *
 * Repository provides an interface to fetch a title or request a new one be generated.
 *
 * Repository modules handle data operations. They provide a clean API so that the rest of the app
 * can retrieve this data easily. They know where to get the data from and what API calls to make
 * when data is updated. You can consider repositories to be mediators between different data
 * sources, in our case it mediates between a network API and an offline database cache.
 */
class CarRepository(private val webService: IApiService, private val dataSource: CarsDatabaseDao) {

    /**
     * [LiveData] to load data.
     *
     * This is the main interface for loading data . The data will be loaded from the offline
     * cache.
     *
     * Observing this will not cause the title to be refreshed, use [TitleRepository.refreshTitle]
     * to refresh the title.
     */
    val title: LiveData<String?> = titleDao.titleLiveData.map { it?.title }

    /**
     * Refresh the current title and save the results to the offline cache.
     *
     * This method does not return the new title. Use [TitleRepository.title] to observe
     * the current tile.
     */
    suspend fun refreshTitle() {
        try {
            val result = withTimeout(5_000) {
                //network.fetchNextTitle()
            }
            //titleDao.insertTitle(Title(result))
        } catch (error: Throwable) {
            //throw TitleRefreshError("Unable to refresh title", error)
        }
    }


    suspend fun getCars(): List<CarProperty> {

    }

    suspend fun getCarById(id: Int): CarProperty {

    }

    suspend fun getCarSpecifications(carId: Int): CarSpecificationsProperty {

    }

    suspend fun getCarImages(carId: Int): CarImageProperty {

    }

    /**
     * Thrown when there was a error fetching a car
     *
     * @property message user ready error message
     * @property cause the original cause of this exception
     */
    class CarFetchingError(message: String, cause: Throwable) : Throwable(message, cause)

    /**
     * This API is exposed for callers from the Java Programming language.
     *
     * The request will run unstructured, which means it won't be able to be cancelled.
     *
     * @param carRefreshCallback a callback
     */
    fun refreshCarInterop(carRefreshCallback: CarRefreshCallback) {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            try {
                refreshTitle()
                carRefreshCallback.onCompleted()
            } catch (throwable: Throwable) {
                carRefreshCallback.onError(throwable)
            }
        }
    }
}

interface CarRefreshCallback {
    fun onCompleted()
    fun onError(cause: Throwable)
}

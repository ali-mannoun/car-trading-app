package com.example.projectapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.projectapp.database.*
import com.example.projectapp.domain.Car
import com.example.projectapp.domain.CarSpecifications
import com.example.projectapp.network.*
import kotlinx.coroutines.*

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
    /*
    The list of words is a public property. It's initialized by getting the LiveData list of words from Room;
    we can do this because of how we defined the getAlphabetizedWords method to return LiveData in the "The LiveData class" step.
    Room executes all queries on a separate thread.
    Then observed LiveData will notify the observer on the main thread when the data has changed.
     */
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val cars: LiveData<List<Car>> = Transformations.map(dataSource.getAllCars()) {
        it.asCarsDomainModel()
    }

    suspend fun fetchFavouriteCarsList(userId: String): List<Car>? {
        val response = webService.fetchFavouriteCars(userId)
        if (response.isSuccessful) {
            Log.e("response favourite", response.body()?.size.toString())
            return response.body()?.asCarDomainModel()
        }
        return null
    }

    suspend fun addCarToFavouriteList(userId: String, carId: Long): Boolean {
        val response = webService.addCarToFavouriteList(FavouriteCarProperty(carId.toString()), userId)
        return response.isSuccessful
    }

    suspend fun removeCarFromFavouriteList(userId: String, carId: Long): Boolean {
        val response = webService.removeCarFromFavouriteList(userId, carId.toString())
        return response.isSuccessful
    }

    suspend fun getCarFavouriteStatus(userId: String, carId: Long): Boolean {
        // val ids = FavouriteProperty(userId, carId.toString())
        val response = webService.checkIfCarInFavouriteList(userId, carId.toString())
        Log.e("response", response.isSuccessful.toString())
        return response.isSuccessful
    }

    /**
     * Refresh the cars stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     */
    suspend fun refreshCars(userId: String = "-1") {
        withContext(Dispatchers.IO) {
            if (userId == "-1") {
                Log.e("response main", "")
                val cars = webService.getCarsProperties()
                cars.body()?.let {
                    dataSource.insertAll(it.asCarsDatabaseModel())
                }
//                 } else {
//                     val cars = webService.fetchFavouriteCars(userId)
//                     Log.e("response favourite", "")
//                     cars.body()?.let {
//                         dataSource.deleteAll()
//                         dataSource.insertAll(it.asCarsDatabaseModel())
//                     }
            }
        }
    }

    /**
     * Refresh the current title and save the results to the offline cache.
     *
     * This method does not return the new title. Use [CarRepository.carsProperty] to observe
     * the current tile.
     */

    /*
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
*/
/*
    /**
     * Get a list of all cars in the remote db
     */
    suspend fun getCars(): List<Car>? {
        val response = webService.getCarsProperties()
        if (response.isSuccessful) {
            return response.body()?.asCarDomainModel()
        }
        return null
    }
*/
    suspend fun getCarSpecificationsById(id: Long): CarSpecifications? {
        val response = webService.getCarSpecifications(id.toInt())
        if (response.isSuccessful) {
            return response.body()?.asCarSpecificationsDomainModel()
        }
        return null
    }
/*
    suspend fun getCarImages(carId: Int): CarImageProperty {

    }*/

//////////////////////////////////////////////////////////////////////////////////
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
                //refreshTitle()
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

package com.example.projectapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Defines methods for using the SleepNight class with Room.
 */
@Dao
interface CarsDatabaseDao {

    //Main Cars operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cars: List<CarEntity>)
/*
    @Query("DELETE FROM cars")
    suspend fun deleteAll()
*/
    @Query("SELECT * FROM cars")
    fun getAllCars(): LiveData<List<CarEntity>>
/*
    // Favourite List
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(car: FavouriteCarEntity)

    @Delete
    suspend fun delete(car: FavouriteCarEntity)
*/
}
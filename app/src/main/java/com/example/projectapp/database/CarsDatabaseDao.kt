package com.example.projectapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

/**
 * Defines methods for using the SleepNight class with Room.
 */
@Dao
interface CarsDatabaseDao {
    /*
@Dao
interface SleepDatabaseDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertAll( videos: List<DatabaseVideo>)

    @Insert
    fun insert(night: SleepNight)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param night new value to write
     */
    @Update
    fun update(night: SleepNight)

    /**
     * Selects and returns the row that matches the supplied start time, which is our key.
     *
     * @param key startTimeMilli to match
     */
    @Query("SELECT * from daily_sleep_quality_table WHERE nightId = :key")
    fun get(key: Long): SleepNight?

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM daily_sleep_quality_table")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC")
    fun getAllNights(): LiveData<List<SleepNight>>

    /**
     * Selects and returns the latest night.
     */
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
    fun getTonight(): SleepNight?

}


     */
}
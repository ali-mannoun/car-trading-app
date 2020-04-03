package com.example.projectapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database entities go in this file. These are responsible for reading and writing from the
 * database.
 */

@Entity(tableName = "car")
data class CarEntity(
        @PrimaryKey
        val id: String,
        @ColumnInfo(name = "company_id")
        val companyId: String,
        //TODO store the image itself into the database
        @ColumnInfo(name = "main_image_url")
        val mainImageUrl: String,
        @ColumnInfo(name = "brand")
        val brand: String,
        @ColumnInfo(name = "model")
        val model: String,
        @ColumnInfo(name = "generation")
        val generation: String,
        @ColumnInfo(name = "year_of_putting_into_production")
        val yearOfPuttingIntoProduction: String,
        @ColumnInfo(name = "year_of_stopping_production")
        val yearOfStoppingProduction: String,
        @ColumnInfo(name = "description")
        val description: String
)

/**
 * Map CarEntity to domain entities
 */
/*
TODO
fun List<CarEntity>.asDomainModel(): List<DevByteVideo> {
    return map {
        DevByteVideo(
                url = it.url,
                title = it.title,
                description = it.description,
                updated = it.updated,
                thumbnail = it.thumbnail)
    }
}
*/
package com.example.projectapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.projectapp.domain.Car
import com.example.projectapp.domain.CarSpecifications
import com.example.projectapp.network.CarProperty

/**
 * Database entities go in this file. These are responsible for reading and writing from the
 * database.
 */

//TODO check the foriegn key (test) ONDELETE CASCADE.
/*
@Entity(tableName = "favourite_cars",
        foreignKeys = [
            ForeignKey(entity = CarEntity::class, parentColumns = arrayOf("id"), childColumns = arrayOf("car_id"))
        ])*/

@Entity(tableName = "favourite_cars")
data class FavouriteCarEntity(
        @PrimaryKey
        val id: String,
        @ColumnInfo(name = "car_id")
        val carId: String
//the rest
)


/**
 * Map [FavouriteCarEntity] to [CarSpecifications] domain entities
 */
/*
fun FavouriteCarEntity.asCarsDomainModel(): CarSpecifications {
    return CarSpecifications(

    )
}
*/

@Entity(tableName = "cars")
data class CarEntity(
        @PrimaryKey
        val id: String,
        @ColumnInfo(name = "company_id")
        val companyId: String,
        @ColumnInfo(name = "main_image_url")
        val mainImageUrl: String,
        //TODO@ColumnInfo(name = "main_image")
        //val mainImag: Bitmap,
        @ColumnInfo(name = "brand")
        val brand: String,
        @ColumnInfo(name = "model")
        val model: String,
        @ColumnInfo(name = "max_speed")
        val maxSpeed: String)

/**
 * Map [CarEntity] to [Car] domain entities
 */
fun List<CarEntity>.asCarsDomainModel(): List<Car> {
    return map {
        Car(
                id = it.id,
                brand = it.brand,
                model = it.model,
                mainImageUrl = it.mainImageUrl,
                maxSpeed = it.maxSpeed,
                companyId = it.companyId
        )
    }
}

/**
 * Map [CarEntity] to [Car] domain entities
 */
fun List<CarProperty>.asCarsDatabaseModel(): List<CarEntity> {
    return map {
        CarEntity(
                id = it.id,
                brand = it.brand,
                model = it.model,
                mainImageUrl = it.imageUrl,
                maxSpeed = it.maxSpeed,
                companyId = it.companyId
        )
    }
}

/**
 * Map [Car] to [CarEntity] domain entities
 */
/*
fun CarSpecifications.asFavouriteCarEntityDatabaseModel(): FavouriteCarEntity {
    return FavouriteCarEntity(
    )
}*/
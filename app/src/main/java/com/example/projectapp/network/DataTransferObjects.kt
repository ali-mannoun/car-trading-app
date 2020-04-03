package com.example.projectapp.network

import com.example.projectapp.domain.CarSpecifications
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 *
 * @see domain package for
 */

@JsonClass(generateAdapter = true)
data class CarProperty(
        @field:Json(name = "id") val id: String,
        @field:Json(name = "brand") val brand: String,
        @field:Json(name = "model") val model: String,
        @field:Json(name = "main_image_url") val imageUrl: String,
        @field:Json(name = "max_speed") val maxSpeed: String
)

@JsonClass(generateAdapter = true)
data class CarSpecificationsProperty(
        @field:Json(name = "id") val id: String,
        @field:Json(name = "company_id") val companyId: String,
        @field:Json(name = "brand") val brand: String,
        @field:Json(name = "model") val model: String,
        @field:Json(name = "main_image_url") val mainImageUrl: String,
        @field:Json(name = "max_speed") val maxSpeed: String,
        @field:Json(name = "generation") val generation: String,
        @field:Json(name = "year_of_putting_into_production") val yearOfPuttingIntoProduction: String,
        @field:Json(name = "year_of_stopping_production") val yearOfStoppingProduction: String,
        @field:Json(name = "description") val description: String,
        @field:Json(name = "power") val power: String,
        @field:Json(name = "model_engine") val modelEngine: String,
        @field:Json(name = "maximum_engine_speed") val maxEngineSpeed: String,
        @field:Json(name = "engine_oil_capacity") val engineOilCapacity: String,
        @field:Json(name = "fuel_system") val fuelSystem: String,
        @field:Json(name = "acceleration_100km/h") val acceleration100kmh: String,
        @field:Json(name = "fuel_consumption") val fuelConsumption: String,
        @field:Json(name = "co2_emissions") val co2Emissions: String,
        @field:Json(name = "seats") val seats: String,
        @field:Json(name = "doors") val doors: String,
        @field:Json(name = "length") val length: String,
        @field:Json(name = "width") val width: String,
        @field:Json(name = "height") val height: String,
        @field:Json(name = "max_weight") val maxWeight: String,
        @field:Json(name = "body_type") val bodyType: String,
        @field:Json(name = "fuel_tank_volume") val fuelTankVolume: String,
        @field:Json(name = "brakes") val brakes: String,
        @field:Json(name = "number_of_gears") val numberOfGears: String,
        @field:Json(name = "gear_type") val gearType: String,
        @field:Json(name = "tire_size") val tireSize: String,
        @field:Json(name = "exterior_features") val exteriorFeatures: String,
        @field:Json(name = "interior_features") val interiorFeatures: String,
        @field:Json(name = "created_at") val createdAt: String,
        @field:Json(name = "updated_at") val updatedAt: String,
        @field:Json(name = "car_images") val carImagesUrls: List<CarImageProperty>
)

@JsonClass(generateAdapter = true)
data class CarImageProperty(
        @field:Json(name = "id") val id: String,
        @field:Json(name = "image_url") val imageUrl: String
)

@JsonClass(generateAdapter = true)
data class RegisterProperty(
        val name: String,
        val email: String,
        val password: String
)

@JsonClass(generateAdapter = true)
data class LoginProperty(
        val email: String,
        val password: String
)

@JsonClass(generateAdapter = true)
data class UserProperty(
        @field:Json(name = "id") val id: Int,
        @field:Json(name = "email") val email: String,
        @field:Json(name = "name") val name: String,
        @field:Json(name = "verification_token") val verification_token: String,
        @field:Json(name = "user_verification") val user_verification: String,
        @field:Json(name = "account_type") val account_type: String,
        @field:Json(name = "user_type") val user_type: String
)

@JsonClass(generateAdapter = true)
data class ResponseProperty(
        @field:Json(name = "message") val message: String,
        @field:Json(name = "code") val code: Int
)

/**
 * Convert Network results to CarImage domain objects
 */
fun List<CarImageProperty>.asCarImageDomainModel(): List<CarSpecifications.CarImage> {
    return this.map {
        CarSpecifications.CarImage(
                id = it.id,
                imageUrl = it.imageUrl
        )
    }
}
/**
 * Convert Network results to CarSpecifications domain objects
 */
fun List<CarSpecificationsProperty>.asCarSpecificationsDomainModel(): List<CarSpecifications> {
    return this.map {
        CarSpecifications(
                generalInformation = CarSpecifications.GeneralInformation(
                        id = it.id,
                        companyId = it.companyId,
                        mainImageUrl = it.mainImageUrl,
                        brand = it.brand,
                        model = it.model,
                        generation = it.generation,
                        yearOfPuttingIntoProduction = it.yearOfPuttingIntoProduction,
                        yearOfStoppingProduction = it.yearOfStoppingProduction,
                        description = it.description
                ),
                engine = CarSpecifications.InternalCombustionEngine(
                        power = it.power,
                        model = it.model,
                        maxSpeed = it.maxEngineSpeed,
                        oilCapacity = it.engineOilCapacity,
                        fuelSystem = it.fuelSystem

                ),
                performance = CarSpecifications.Performance(
                        maxSpeed = it.maxSpeed,
                        acceleration100Kmh = it.acceleration100kmh,
                        fuelConsumption = it.fuelConsumption,
                        co2Emissions = it.co2Emissions
                ),
                body = CarSpecifications.Body(
                        seats = it.seats,
                        doors = it.doors,
                        length = it.length,
                        width = it.width,
                        height = it.height,
                        maxWeight = it.maxWeight,
                        bodyType = it.bodyType,
                        fuelTankVolume = it.fuelTankVolume
                ),
                others = CarSpecifications.Others(
                        brakes = it.brakes,
                        numberOfGears = it.numberOfGears,
                        gearType = it.gearType,
                        tireSize = it.tireSize,
                        exteriorFeatures = it.exteriorFeatures,
                        interiorFeatures = it.interiorFeatures
                ),
                images = it.carImagesUrls.asCarImageDomainModel()
        )
    }
}


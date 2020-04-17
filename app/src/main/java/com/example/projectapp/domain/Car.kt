package com.example.projectapp.domain

import com.example.projectapp.utils.smartTruncate


/**
 * Domain objects are plain Kotlin data classes that represent the things in our app. These are the
 * objects that should be displayed on screen, or manipulated by the app.
 *
 * @see database for objects that are mapped to the database
 * @see network for objects that parse or prepare network calls
 */

data class Car(
        val id: String,
        val brand: String,
        val model: String,
        val mainImageUrl: String,
        val maxSpeed: String,
        val companyId: String
)


data class CarSpecifications(val generalInformation: GeneralInformation,
                        val engine: InternalCombustionEngine,
                        val performance: Performance,
                        val body: Body,
                        val others: Others,
                        val images: List<CarImage>) {


    class GeneralInformation(
            val id: String,
            val companyId: String,
            val mainImageUrl: String,
            val brand: String,
            val model: String,
            val generation: String,
            val yearOfPuttingIntoProduction: String,
            val yearOfStoppingProduction: String,
            val description: String
    )

    class InternalCombustionEngine(
            val power: String,
            val model: String,
            val maxSpeed: String,
            val oilCapacity: String,
            val fuelSystem: String
    )

    class Performance(
            val maxSpeed: String,
            val acceleration100Kmh: String,
            val fuelConsumption: String,
            val co2Emissions: String
    )

    class Body(
            val seats: String,
            val doors: String,
            val length: String,
            val width: String,
            val height: String,
            val maxWeight: String,
            val bodyType: String,
            val fuelTankVolume: String
    )

    class Others(
            val brakes: String,
            val numberOfGears: String,
            val gearType: String,
            val tireSize: String,
            val exteriorFeatures: String,
            val interiorFeatures: String
    )

    class CarImage(
            val id: String,
            val imageUrl: String
    )

    /**
     * Short description is used for displaying truncated descriptions in the UI
     */
    val shortDescription: String
        get() = generalInformation.description.smartTruncate(200)
}
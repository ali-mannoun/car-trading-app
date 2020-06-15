package com.example.projectapp.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

// Since we only have one service, this can all go in one file.
// If you add more services, split this to multiple files and make sure to share the retrofit
// object between services.

private const val BASE_URL = "http://192.168.1.102/api/"
private const val ACCESS_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiM2U1YmQxZmE4YmJkZTk4ZTU1NDNjMTBlMGEwNWNkN2NkMmViMjAzZDM0ZmUxZjhkMGY5Yjg0MzFkNTA3ZDBlNjQ1MjNlMDA1OGUyMWE0ZDMiLCJpYXQiOjE1ODMzMTczMTIsIm5iZiI6MTU4MzMxNzMxMiwiZXhwIjoxNjE0ODUzMzExLCJzdWIiOiI2Iiwic2NvcGVzIjpbXX0.qQE9bQj9j3FEn8s0BmYlfYtdEbGIbsSC9ZgSPZAEo0RKVFxtOUHjWWtzXyDBR-HvtHKE7DuAKqmrrsvPbFgiEJKk4BJOnNwDGZ_oERBAjBxs4y3Tw78ukqPiB-o-gylIy2IqHX7L-QExzJaCbyPotBP2-Hpk0aKjNQTVoT9Y_D0oAYCYf4hBdtU5LnV4lDIoZ_uVZaB2sA1h2jg0367kH_FSo_TbqBBvp0s-6_Hh6j6UVbhUTRwVaPZJ0j2p9awSxNOql1UaRvqcY8WauQO2xn9mVffsKqFc5H6-aK8Ayje-0Ch0RyF6kz7_Lfw0LTGIYylaKdWDRYTYyBl2dvRmtoLsDF4z-TDBWOu3aCULJmQHSoNavhNYT21zY99zddWt7CpjbITeF8vN4oSOqlC_TjtYvQPccBcF95nFsgf5RzDKpKQHhTWbp0p67da8L_5pbB6dPByArwCfas4pk6pD1mmH6N-5BFg-Qk6lWOXhMVhzGipz1ZuUQCZh_8JTHUmqBoxDwH84sFLo9dKHhuUFS7FxzPpCLgCMC2orG5srBZzfrVrMn0BCmAFqG47sWnZNorVIte-Z6UEG6Pj5RErZdoZja_m3Pd6Ns6e8myioCZJ5UlFFlG0Q-mm2gpJ6neOpQIlorPfzmtMi24LAE8fKp0UfiiAhhoN64eg73VIMRuA"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object pointing to the desired URL .
 */

// Configure retrofit to parse JSON and use coroutines
private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

//public interface to expose getProperties method
interface IApiService {
    /**
     * Returns a Retrofit callback that delivers a String
     * The @GET annotation indicates that the "accounts" endpoint will be requested with the GET
     * HTTP method
     */
    @POST("users/login")
    suspend fun login(@Body user: LoginProperty): Response<UserProperty>

    @POST("users/check-user-credentials")
    suspend fun checkUserCredentials(@Body user: LoginProperty): Response<ResponseProperty>

    @POST("users")
    suspend fun createNewAccount(@Body user: RegisterProperty): Response<UserProperty>

    @GET("users/{user_id}/cars/{car_id}")
    suspend fun checkIfCarInFavouriteList(@Path("user_id") userId: String, @Path("car_id") carId: String): Response<ResponseProperty>

    @POST("users/{user_id}/cars")
    suspend fun addCarToFavouriteList(@Body favouriteCar: FavouriteCarProperty, @Path("user_id") userId: String): Response<ResponseProperty>

    @DELETE("users/{user_id}/cars/{car_id}")
    suspend fun removeCarFromFavouriteList(@Path("user_id") userId: String, @Path("car_id") carId: String): Response<ResponseProperty>

    @GET("users/{user_id}/cars")
    suspend fun fetchFavouriteCars(@Path("user_id") userId: String): Response<List<CarProperty>>

    @GET("users/verify/{token}")
    suspend fun verifyAccount(@Path("token") authCode: String): Response<ResponseProperty>

    @GET("cars")
    suspend fun getCarsProperties(): Response<List<CarProperty>>

    @GET("cars/{id}/specifications")
    suspend fun getCarSpecifications(@Path("id") carId: Int): Response<CarSpecificationsProperty>

    @GET("cars/{id}/specifications/images")
    suspend fun getCarImages(@Path("id") carId: Int): Response<CarSpecificationsProperty>

    @GET("cars/{id}/specifications/images/{id}")
    suspend fun getCarImageById(@Path("id") carId: Int, @Path("id") imageId: Int): Response<CarSpecificationsProperty>

}

/**
 * Main entry point for network access.
 * initialize Retrofit service , Call like `DevByteNetwork.devbytes.getPlaylist()`
 */
private object AccountApi {
    val retrofitWebService: IApiService by lazy { retrofit.create(IApiService::class.java) }
}

fun getNetworkService() = AccountApi.retrofitWebService

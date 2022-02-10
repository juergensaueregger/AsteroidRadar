package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.DatabaseAsteroid
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val scalars = ScalarsConverterFactory.create()

private  val retrofit = Retrofit.Builder()
    .addConverterFactory(scalars)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

interface NasaService {
    @GET("planetary/apod?api_key=8GgRy7s9TPH7LV74DZA1Yxb4WTgXGguCl7w8GK50")
    suspend fun  getPicture(): PictureOfDay

    @GET("neo/rest/v1/feed?detailed=true&api_key=8GgRy7s9TPH7LV74DZA1Yxb4WTgXGguCl7w8GK50")
    suspend fun getAsteroidsString(): String

}

object NasaApi {
    val retrofitService: NasaService by lazy {
        retrofit.create(NasaService::class.java)
    }
}

fun List<Asteroid>.asDatabaseAsteroid(): Array<DatabaseAsteroid> {
    return map {
        DatabaseAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it. closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}

suspend fun getAsteroids() : List<Asteroid>?{
    val asteroidListString = NasaApi.retrofitService.getAsteroidsString()
    return parseAsteroidsJsonResult(JSONObject(asteroidListString))
}



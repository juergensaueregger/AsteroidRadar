package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.asDatabaseAsteroid
import com.udacity.asteroidradar.api.getAsteroids
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asAstroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception


class Repository(private val database:AsteroidDatabase) {

    val astroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroiddao.getAsteroids()) {
        it.asAstroid()
    }
    suspend fun refreshAsteroids(){
        withContext(Dispatchers.IO){
            val asteroidList = getData()
            if ( asteroidList?.size != 0){
                asteroidList?.asDatabaseAsteroid()?.let { database.asteroiddao.insertAll(* it) }
            }
        }
    }


    private suspend fun getData(): List<Asteroid>? {
        return try {
            getAsteroids()
        } catch( e: java.lang.Exception){
            null
        }
    }

    suspend fun getPictureOfTheDay(): PictureOfDay? {
        var picture:PictureOfDay?
        withContext(Dispatchers.IO) {
            try {
                picture = NasaApi.retrofitService.getPicture()
            } catch (e: Exception) {
                picture = null
            }

        }
        return picture
    }
}
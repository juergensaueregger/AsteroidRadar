package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val database = getDatabase(app)
    private val repo = Repository(database)

    private val _navigateIndicator = MutableLiveData<Asteroid?>()
    val navigateIndicator: LiveData<Asteroid?>
        get() = _navigateIndicator

    val pictureOfTheDay = MutableLiveData<PictureOfDay?>()
    val asteroidsList = repo.astroids

    init {
        viewModelScope.launch {
            repo.refreshAsteroids()
            val pictureOfDay = repo.getPictureOfTheDay()
            if (pictureOfDay != null) {
                pictureOfTheDay.value = pictureOfDay
            }
        }
    }

    fun setNavigateIndicator(argument_send_to_clicked: Asteroid) {
        _navigateIndicator.value = argument_send_to_clicked
    }

    fun unsetNavigateIndicator() {
        _navigateIndicator.value = null
    }
}
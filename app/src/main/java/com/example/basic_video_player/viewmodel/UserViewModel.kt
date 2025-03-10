package com.pubscale.basicvideoplayer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basic_video_player.model.VideoModel
import com.example.basic_video_player.repository.VideoRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * A sealed class to represent different states of API response.
 */
sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>() // Represents a successful response with data
    data class Error(val message: String) : Resource<Nothing>() // Represents an error with a message
    object Loading : Resource<Nothing>() // Represents a loading state
}

/**
 * ViewModel for managing video data.
 * It interacts with the [VideoRepository] to fetch video details and exposes data to the UI.
 */
class VideoViewModel : ViewModel() {

    private val repository = VideoRepository() // Repository instance for network calls
    private val _liveData = MutableLiveData<Resource<VideoModel>>() // Mutable LiveData to store API responses
    val liveData: LiveData<Resource<VideoModel>> = _liveData // Exposed immutable LiveData

    /**
     * Calls the API to fetch video data and updates LiveData accordingly.
     */
    fun callAPI() {
        _liveData.value = Resource.Loading // Notify UI that loading has started

        repository.getListOfModel(object : Callback<VideoModel> {
            override fun onResponse(call: Call<VideoModel>, response: Response<VideoModel>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _liveData.value = Resource.Success(it) // Update LiveData with success response
                    } ?: run {
                        _liveData.value = Resource.Error("Response body is null") // Handle null response body
                    }
                } else {
                    _liveData.value = Resource.Error("Response error: ${response.code()}") // Handle API errors
                }
            }

            override fun onFailure(call: Call<VideoModel>, t: Throwable) {
                val errorMessage = if (t is IOException) {
                    "Network failure. Please check your connection." // Handle network errors
                } else {
                    "Unexpected error occurred." // Handle unexpected errors
                }
                _liveData.value = Resource.Error(errorMessage) // Update LiveData with error
            }
        })
    }
}

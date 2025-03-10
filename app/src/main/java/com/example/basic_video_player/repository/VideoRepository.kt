package com.example.basic_video_player.repository

import com.example.basic_video_player.model.VideoModel
import com.example.basic_video_player.network.ApiClient
import com.example.basic_video_player.network.Network
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.HttpException
import java.io.IOException

/**
 * Repository class for handling video-related network requests.
 * This class abstracts the data layer and provides video data to the ViewModel or UI layer.
 */
class VideoRepository {

    // Creates an instance of the API client using Retrofit
    private val apiClient: ApiClient = Network.getInstance().create(ApiClient::class.java)

    /**
     * Fetches the video data from the API.
     * The response is returned asynchronously using a callback.
     *
     * @param callback The callback to handle success or failure of the API request.
     */
    fun getListOfModel(callback: Callback<VideoModel>) {
        val call: Call<VideoModel> = apiClient.getVideoUrl()

        call.enqueue(object : Callback<VideoModel> {
            override fun onResponse(call: Call<VideoModel>, response: Response<VideoModel>) {
                if (response.isSuccessful && response.body() != null) {
                    // Pass the successful response to the provided callback
                    callback.onResponse(call, response)
                } else {
                    // Handle HTTP error cases by passing an exception to onFailure
                    callback.onFailure(call, HttpException(response))
                }
            }

            override fun onFailure(call: Call<VideoModel>, t: Throwable) {
                // Handles network errors gracefully
                val exception = when (t) {
                    is IOException -> IOException("Network failure. Please check your connection.", t)
                    else -> Exception("Unexpected error occurred.", t)
                }
                callback.onFailure(call, exception)
            }
        })
    }
}

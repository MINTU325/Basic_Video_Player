package com.example.basic_video_player.network

import com.example.basic_video_player.model.VideoModel
import retrofit2.Call
import retrofit2.http.GET


interface ApiClient {
    @GET("video_url.json")  // Relative path
    fun getVideoUrl(): Call<VideoModel>
}
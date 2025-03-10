package com.example.basic_video_player.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the video model.
 * This class is used to parse video-related data from the API response.
 *
 * @property url The URL of the video.
 */
data class VideoModel(

  @field:SerializedName("url") // Maps the JSON field "url" to this property
  val url: String
)

package com.pubscale.basicvideoplayer

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.basic_video_player.R
import com.pubscale.basicvideoplayer.viewmodel.Resource
import com.pubscale.basicvideoplayer.viewmodel.VideoViewModel

/**
 * MainActivity that handles video playback using ExoPlayer and supports Picture-in-Picture (PiP) mode.
 */
class MainActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null
    private var playerView: PlayerView? = null
    private val videoViewModel: VideoViewModel by viewModels() // ViewModel for fetching video URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerView = findViewById(R.id.player_view)
        setupExoPlayer()

        // Fetch video URL from API
        videoViewModel.callAPI()

        // Observe LiveData and update UI accordingly
        videoViewModel.liveData.observe(this) { videoModel ->
            when (videoModel) {
                is Resource.Success -> playVideo(videoModel.data.url) // Play video when API call succeeds
                is Resource.Error -> Toast.makeText(this, videoModel.message, Toast.LENGTH_LONG).show()
                is Resource.Loading -> Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Initializes ExoPlayer but does not set a video source.
     */
    private fun setupExoPlayer() {
        player = ExoPlayer.Builder(this).build()
        playerView?.player = player
    }

    /**
     * Plays a video from the given URL using ExoPlayer.
     * @param videoUrl The URL of the video to be played.
     */
    private fun playVideo(videoUrl: String) {
        val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.playWhenReady = true
    }

    /**
     * Handles entering Picture-in-Picture mode when the user leaves the app.
     * This is only available on Android O (API 26) and above.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        enterPictureInPictureMode(
            PictureInPictureParams.Builder()
                .setAspectRatio(Rational(16, 9)) // Set aspect ratio to match video
                .build()
        )
    }

    /**
     * Handles UI changes when entering or exiting Picture-in-Picture mode.
     * Disables player controls in PiP mode.
     */
    override fun onPictureInPictureModeChanged(isInPipMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPipMode, newConfig)
        playerView?.useController = !isInPipMode
    }

    /**
     * Pauses the video when the activity stops.
     */
    override fun onStop() {
        super.onStop()
        player?.pause()
    }

    /**
     * Releases the player resources when the activity is destroyed.
     */
    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }
}

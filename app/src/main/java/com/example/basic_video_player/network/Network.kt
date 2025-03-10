package com.example.basic_video_player.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton class for managing network requests using Retrofit.
 */
class Network {

    companion object {

        // Interceptor for logging HTTP requests and responses
        private val httpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        /**
         * Provides a singleton instance of Retrofit configured with a base URL,
         * Gson converter, and an HTTP client with logging enabled.
         *
         * @return Retrofit instance for making API requests.
         */
        fun getInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/greedyraagava/test/refs/heads/main/")
                .addConverterFactory(GsonConverterFactory.create()) // Converts JSON responses
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(httpLoggingInterceptor) // Adds logging interceptor
                        .build()
                )
                .build()
        }
    }
}

// Example API Endpoint: https://raw.githubusercontent.com/greedyraagava/test/refs/heads/main/video_url.json

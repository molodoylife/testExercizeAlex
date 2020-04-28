package com.example.testexercisealexm.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun gson(config: GsonBuilder.() -> Unit = {}): Gson =
    GsonBuilder().serializeNulls().setPrettyPrinting().apply(config).create()

fun retrofit(gson: Gson = gson()): Retrofit {
    val client = OkHttpClient.Builder()
        .readTimeout(3, TimeUnit.SECONDS)
        .connectTimeout(3, TimeUnit.SECONDS)
        .apply {

            addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        .build()


    return Retrofit.Builder()
        .baseUrl("https://en.wikipedia.org")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()
}
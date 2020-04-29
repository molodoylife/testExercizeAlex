package com.example.testexercisealexm.di

import android.app.Application
import android.content.Context
import android.location.LocationManager
import com.example.testexercisealexm.api.WikiApi
import com.example.testexercisealexm.api.retrofit
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import retrofit2.create
import javax.inject.Singleton

@Module
object AppModule {
    @Singleton
    @Provides
    fun provideWikiApi(): WikiApi = retrofit().create()

    @Singleton
    @Provides
    fun provideLocationManager(app: Application): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(app)
}

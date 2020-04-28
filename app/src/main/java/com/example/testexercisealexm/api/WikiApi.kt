package com.example.testexercisealexm.api

import com.example.testexercisealexm.api.models.WikiGeoResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WikiApi {
    @GET("/w/api.php?action=query&list=geosearch&gslimit=10&format=json")
    fun getWikiPoints(@Query("gsradius") gsradius: Int, @Query("gscord") gscord: String): Single<WikiGeoResponse>
}


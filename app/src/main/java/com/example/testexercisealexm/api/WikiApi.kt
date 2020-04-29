package com.example.testexercisealexm.api

import com.example.testexercisealexm.api.models.WikiGeoResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WikiApi {
    @GET("/w/api.php?action=query&list=geosearch&gslimit=50&format=json")
    fun getWikiPoints(@Query("gsradius") gsradius: Int, @Query("gscoord") gscord: String): Single<WikiGeoResponse>

    @GET("w/api.php?action=query&prop=info|description|images&format=json")
    fun getWikiPointDetails(@Query("pageids") pageId: Int): Single<WikiGeoResponse>
}


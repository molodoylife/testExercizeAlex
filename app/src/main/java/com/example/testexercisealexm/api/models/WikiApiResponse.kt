package com.example.testexercisealexm.api.models

import com.google.gson.annotations.SerializedName

data class WikiGeoResponse (
    @SerializedName("batchcomplete") val batchcomplete : String,
    @SerializedName("query") val query : Query
)

data class Query (
    @SerializedName("geosearch") val geosearch : List<Geosearch>
)

data class Geosearch (
    @SerializedName("pageid") val pageid : Int,
    @SerializedName("ns") val ns : Int,
    @SerializedName("title") val title : String,
    @SerializedName("lat") val lat : Double,
    @SerializedName("lon") val lon : Double,
    @SerializedName("dist") val dist : Double,
    @SerializedName("primary") val primary : String
)
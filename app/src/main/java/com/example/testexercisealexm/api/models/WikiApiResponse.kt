package com.example.testexercisealexm.api.models

import com.google.gson.annotations.SerializedName

data class WikiGeoResponse(
    @SerializedName("query") val query: Query
)

data class Query(
    @SerializedName("geosearch") val geosearch: List<Geosearch>,
    @SerializedName("pages") val pages: Map<String, WikiSummary>
)

data class Geosearch(
    @SerializedName("pageid") val pageid: Int,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double
)

data class WikiSummary(
    @SerializedName("pageid") val pageid: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("images") val images: List<Images>?
)

data class Images(
    @SerializedName("title") val title: String
)
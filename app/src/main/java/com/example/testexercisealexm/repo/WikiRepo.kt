package com.example.testexercisealexm.repo

import com.example.testexercisealexm.api.WikiApi
import com.example.testexercisealexm.api.models.WikiGeoResponse
import com.example.testexercisealexm.domain.model.WikiPoiDetails
import com.example.testexercisealexm.domain.model.WikiPoint
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import javax.inject.Inject

/**
 * Repo to access data source
 * */
interface WikiRepo {
    fun getNearestWikiPois(radius: Int, lat: Double, lon: Double): Single<List<WikiPoint>>

    fun getWikiPoiDetails(wikiPoint: WikiPoint): Single<WikiPoiDetails>
}

class WikiRepoImp @Inject constructor(private val wikiApi: WikiApi) : WikiRepo {
    override fun getNearestWikiPois(
        radius: Int,
        lat: Double,
        lon: Double
    ): Single<List<WikiPoint>> {
        return wikiApi.getWikiPoints(radius, "$lat|$lon").map {
            it.toWikiPoints()
        }
    }

    override fun getWikiPoiDetails(wikiPoint: WikiPoint): Single<WikiPoiDetails> {
        return wikiApi.getWikiPointDetails(wikiPoint.pageId).map {
            it.toWikiDetails(wikiPoint.position)
        }
    }
}

/**
 * Mapper for converting api model to domain model
 * */
fun WikiGeoResponse.toWikiPoints(): List<WikiPoint> {
    val result = mutableListOf<WikiPoint>()
    val geoSearchList = this.query.geosearch

    // Creating a list of domain WikiPoint models
    geoSearchList.forEach {
        result.add(WikiPoint(it.pageid, LatLng(it.lat, it.lon)))
    }

    return result
}

/**
 * Mapper for converting api model to domain model
 * */
fun WikiGeoResponse.toWikiDetails(coords: LatLng): WikiPoiDetails {

    // Url address for accessing wiki images by file name
    val wikiImageUrlAddress = "https://commons.wikimedia.org/wiki/Special:FilePath/"
    val wikiImageSizeString = "?width=200"

    // Get always first page because we request only for a single one
    val page = this.query.pages.values.toList()[0]

    var imagesList = listOf<String>()

    // receive a list of images as a list of String urls to create business WikiPoiDetails model
    page.images?.let {
        imagesList = it.map { image ->
            wikiImageUrlAddress + image.title.replace("\\s".toRegex(), "_") + wikiImageSizeString
        }
    }

    // Return business model to domain layer. If title or description are null then return an empty String
    return WikiPoiDetails(page.pageid, page.title ?: "", page.description ?: "", imagesList, coords)
}




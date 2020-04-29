package com.example.testexercisealexm.repo

import com.example.testexercisealexm.api.WikiApi
import com.example.testexercisealexm.api.models.WikiGeoResponse
import com.example.testexercisealexm.domain.model.WikiPoiDetails
import com.example.testexercisealexm.domain.model.WikiPoint
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import javax.inject.Inject

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

fun WikiGeoResponse.toWikiPoints(): List<WikiPoint> {
    val result = mutableListOf<WikiPoint>()
    val geoSearchList = this.query.geosearch
    geoSearchList.forEach {
        result.add(WikiPoint(it.pageid, LatLng(it.lat, it.lon)))
    }

    return result
}

fun WikiGeoResponse.toWikiDetails(coords: LatLng): WikiPoiDetails {

    val page = this.query.pages.values.toList()[0]

    var imagesList = page.images.map {
        "https://commons.wikimedia.org/wiki/Special:FilePath/${it.title.replace(
            "\\s".toRegex(),
            "_"
        )}?width=200"
    }

    return WikiPoiDetails(page.pageid, page.title, page.description ?: "", imagesList, coords)
}




package com.example.testexercisealexm.repo

import com.example.testexercisealexm.api.WikiApi
import com.example.testexercisealexm.api.models.WikiGeoResponse
import com.example.testexercisealexm.domain.model.WikiPoint
import io.reactivex.Single
import javax.inject.Inject

interface WikiRepo {
    fun getNearestWikiPois(radius: Int, lat: Double, lon: Double): Single<List<WikiPoint>>
}

class WikiRepoImp @Inject constructor(private val wikiApi: WikiApi) : WikiRepo {
    override fun getNearestWikiPois(radius: Int, lat: Double, lon: Double): Single<List<WikiPoint>> {
        return wikiApi.getWikiPoints(radius, "$lat|$lon").map {
            it.toWikiPoints()
        }
    }
}

fun WikiGeoResponse.toWikiPoints(): List<WikiPoint> {
    val result = mutableListOf<WikiPoint>()
    val geoSearchList = this.query.geosearch
    geoSearchList.forEach {
        result.add(WikiPoint(it.pageid, it.lat, it.lon))
    }

    return result
}




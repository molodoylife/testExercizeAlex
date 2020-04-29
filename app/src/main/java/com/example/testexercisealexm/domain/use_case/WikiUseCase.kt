package com.example.testexercisealexm.domain.use_case

import com.example.testexercisealexm.domain.model.WikiPoiDetails
import com.example.testexercisealexm.domain.model.WikiPoint
import com.example.testexercisealexm.repo.WikiRepo
import io.reactivex.Single
import javax.inject.Inject

interface WikiUseCase {
    fun getNearestPois(radius: Int, lat: Double, lon: Double): Single<List<WikiPoint>>

    fun getPoiDetails(point: WikiPoint): Single<WikiPoiDetails>
}

class WikiUseCaseImp @Inject constructor(private val wikiRepo: WikiRepo): WikiUseCase{
    override fun getNearestPois(radius: Int, lat: Double, lon: Double): Single<List<WikiPoint>> {
        val adjustedRadius = if(radius in 10..10000) radius else 10000

        return wikiRepo.getNearestWikiPois(adjustedRadius, lat, lon)
    }

    override fun getPoiDetails(point: WikiPoint): Single<WikiPoiDetails> {
        return wikiRepo.getWikiPoiDetails(point)
    }
}
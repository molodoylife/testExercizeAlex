package com.example.testexercisealexm.domain.use_case

import com.example.testexercisealexm.domain.model.WikiPoint
import com.example.testexercisealexm.repo.WikiRepo
import io.reactivex.Single
import javax.inject.Inject

interface WikiUseCase {
    fun getNearestPois(radius: Int, lat: Double, lon: Double): Single<List<WikiPoint>>
}

class WikiUseCaseImp @Inject constructor(private val wikiRepo: WikiRepo): WikiUseCase{
    override fun getNearestPois(radius: Int, lat: Double, lon: Double): Single<List<WikiPoint>> {
        return wikiRepo.getNearestWikiPois(radius, lat, lon)
    }
}
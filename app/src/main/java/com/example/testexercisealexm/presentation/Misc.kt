package com.example.testexercisealexm.presentation

import android.location.Location
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.VisibleRegion

const val POI_DETAILS_KEY = "POI_DETAILS_KEY"
const val POI_DETAILS_FRAGMENT_TAG = "POI_DETAILS_FRAGMENT_TAG"

fun getMapRadius(googleMap: GoogleMap): Int {
    val visibleRegion: VisibleRegion = googleMap.projection.visibleRegion

    val diagonalDistance = FloatArray(1)

    val farLeft = visibleRegion.farLeft
    val nearRight = visibleRegion.nearRight

    Location.distanceBetween(
        farLeft.latitude,
        farLeft.longitude,
        nearRight.latitude,
        nearRight.longitude,
        diagonalDistance
    )

    return (diagonalDistance[0] / 2).toInt()
}
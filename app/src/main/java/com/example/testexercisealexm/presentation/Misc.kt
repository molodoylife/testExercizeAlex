package com.example.testexercisealexm.presentation

import android.location.Location
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.VisibleRegion


//TODO Organize in separate files

/**
 * Constants used in app
 * */
const val POI_DETAILS_KEY = "POI_DETAILS_KEY"
const val CURRENT_LAT_KEY = "CURRENT_LAT_KEY"
const val CURRENT_LON_KEY = "CURRENT_LON_KEY"
const val POI_IMAGES_KEY = "POI_IMAGES_KEY"
const val POI_DETAILS_FRAGMENT_TAG = "POI_DETAILS_FRAGMENT_TAG"


/**
 * @return radius of visible google map region
 * */
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

@GlideModule
class GlideModule : AppGlideModule()
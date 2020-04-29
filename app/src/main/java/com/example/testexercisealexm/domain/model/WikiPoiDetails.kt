package com.example.testexercisealexm.domain.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WikiPoiDetails(val poiId: Int, val title: String, val description: String, val images: List<String>, val coords: LatLng): Parcelable
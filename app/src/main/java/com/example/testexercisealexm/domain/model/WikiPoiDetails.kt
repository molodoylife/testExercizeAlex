package com.example.testexercisealexm.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WikiPoiDetails(val poiId: Int, val title: String, val description: String, val images: List<String>): Parcelable
package com.android.ntduc.baseproject.data.dto.lottie

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Lottie(
    @Json(name = "assets")
    var assets: ArrayList<Assets> = arrayListOf()
) : Parcelable
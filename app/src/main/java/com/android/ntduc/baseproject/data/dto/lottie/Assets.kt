package com.android.ntduc.baseproject.data.dto.lottie

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@JsonClass(generateAdapter = true)
@Parcelize
data class Assets(
    @Json(name = "id")
    var id: String? = null,
    @Json(name = "w")
    var w: Int? = null,
    @Json(name = "h")
    var h: Int? = null,
    @Json(name = "u")
    var u: String? = null,
    @Json(name = "p")
    var p: String? = null,
    @Json(name = "e")
    var e: Int? = null
) : Parcelable
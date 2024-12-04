package org.qstuff.edc_app.domain.entity

import androidx.annotation.Keep

@Keep
data class Poi(
    val name: String,
    var shareLink: String,
    val category: String,
    val mainLocationLat: Double,
    val mainLocationLong: Double
)
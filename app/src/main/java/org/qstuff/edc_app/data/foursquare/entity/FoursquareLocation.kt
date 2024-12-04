package org.qstuff.edc_app.data.foursquare.entity

import androidx.annotation.Keep

@Keep
data class FoursquareLocation(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
package org.qstuff.edc_app.data.foursquare.entity

import androidx.annotation.Keep

@Keep
data class FoursquareNearbyResponseDto(
    val results: List<FoursquareNearbyItem>
)
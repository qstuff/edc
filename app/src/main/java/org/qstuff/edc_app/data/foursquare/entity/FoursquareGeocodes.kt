package org.qstuff.edc_app.data.foursquare.entity

import androidx.annotation.Keep

@Keep
data class FoursquareGeocodes(
    val dropOff: FoursquareLocation = FoursquareLocation(),
    val frontDoor: FoursquareLocation = FoursquareLocation(),
    val main: FoursquareLocation = FoursquareLocation(),
    val road: FoursquareLocation = FoursquareLocation(),
    val roof: FoursquareLocation = FoursquareLocation()
)
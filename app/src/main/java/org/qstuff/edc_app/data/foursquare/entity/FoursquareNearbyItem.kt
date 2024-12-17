package org.qstuff.edc_app.data.foursquare.entity

import androidx.annotation.Keep
import org.qstuff.edc_app.domain.entity.Poi

// TODO: for now we use a minimal set of the possibly retrievable data.
// TODO: just enough to fulfill the basic feature requirements
@Keep
data class FoursquareNearbyItem(
    val name: String,
    val link: String,
    val categories: List<FoursquareCategory>,
    val geocodes: FoursquareGeocodes
) {
    fun mapToPoi() =
        Poi(
            name = name,
            shareLink = createShareLink(),
            category = categories.firstOrNull()?.name ?: "No category available ",
            mainLocationLat = geocodes.main.latitude,
            mainLocationLong = geocodes.main.longitude
        )

    // TODO: the "link" that comes with the nearby call does not contain
    // TODO: a base URL and includes an API version info:
    // TODO: v3/places/560d09a0498e04e7a4318441
    // TODO: When i add a base URL and modify the link as below things are working

    fun createShareLink() =
        "https://foursquare.com/v${link.substring(3)}"
}
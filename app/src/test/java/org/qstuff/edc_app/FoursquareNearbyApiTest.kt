package org.qstuff.edc_app

import org.junit.Assert.assertNotNull
import org.junit.Test
import org.qstuff.edc_app.data.foursquare.entity.FoursquareCategory
import org.qstuff.edc_app.data.foursquare.entity.FoursquareGeocodes
import org.qstuff.edc_app.data.foursquare.entity.FoursquareLocation
import org.qstuff.edc_app.data.foursquare.entity.FoursquareNearbyItem

class FoursquareNearbyApiTest {

    private val testFoursquareNearbyItem =
        FoursquareNearbyItem(
            name = "Evenly HQ",
            categories = listOf(
                FoursquareCategory(name = "Business and Professional Services")
            ),
            link = "/v3/places/560d09a0498e04e7a4318441",
            geocodes = FoursquareGeocodes(
                dropOff = FoursquareLocation(
                    latitude = 52.499729,
                    longitude = 13.424916
                ),
                main = FoursquareLocation(
                    latitude = 52.500055,
                    longitude = 13.425007
                ),
                roof = FoursquareLocation(
                    latitude = 52.500055,
                    longitude = 13.425007
                ),
            )
    )

    @Test
    fun test_mapFoursquareNearbyItemToPoi() {

        val poi = testFoursquareNearbyItem.mapToPoi()
        poi.shareLink = testFoursquareNearbyItem.createShareLink()

        assertNotNull(poi)
        assert(poi.name == "Evenly HQ")
        assert(poi.shareLink == "https://foursquare.com/v${testFoursquareNearbyItem.link.substring(3)}")
        assert(poi.category == "Business and Professional Services")
        assert(poi.mainLocationLat >= 0.0)
        assert(poi.mainLocationLat == 52.500055)
        assert(poi.mainLocationLong >= 0.0)
        assert(poi.mainLocationLong == 13.425007)
    }
}
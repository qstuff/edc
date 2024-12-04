package org.qstuff.edc_app.data.foursquare.datasource

import org.qstuff.edc_app.data.foursquare.entity.FoursquareNearbyResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API requests from Foursquare Places API
 * See:
 * https://docs.foursquare.com/developer/docs/foursquare-sdks-overview
 */
interface FoursquarePlacesApiService {

    @GET("places/nearby")
    suspend fun getNearbyPoi(
        @Query("ll") latLong: String,
        @Query("hacc") radius: String,
        @Query("limit") limit: String
    ) : Response<FoursquareNearbyResponseDto>
}
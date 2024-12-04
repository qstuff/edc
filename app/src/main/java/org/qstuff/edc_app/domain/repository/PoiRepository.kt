package org.qstuff.edc_app.domain.repository

import org.qstuff.edc_app.domain.entity.ApiError
import org.qstuff.edc_app.domain.entity.Poi

/**
 * Provides POI data independently of the underlying data layer.
 * I.e. we do not care here where the data comes from. We just want a List of Poi.
 */
interface PoiRepository {

    suspend fun getNearbyPoi(
        locationLatLng: Pair<Double, Double>,
        radius: Int,
        limit: Int,
        setError: (ApiError) -> Unit
    ): List<Poi>
}
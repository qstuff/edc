package org.qstuff.edc_app.domain.usecase

import kotlinx.coroutines.flow.flow
import org.qstuff.edc_app.domain.entity.ApiError
import org.qstuff.edc_app.domain.entity.Poi
import org.qstuff.edc_app.domain.repository.PoiRepository

/**
 * Load a list of [Poi] from [PoiRepository].
 * Returns a Flow delivering a List of POI or an empty List in case of any Error
 */
class GetNearbyPoiUseCase(
    private val poiRepository: PoiRepository
) {
    suspend fun execute(
        locationLatLng: Pair<Double, Double>,
        radius: Int,
        limit: Int,
        setError: (ApiError) -> Unit
    ) = flow {
        emit(
            poiRepository.getNearbyPoi(
                locationLatLng = locationLatLng,
                radius = radius,
                limit = limit,
                setError = setError
            )
        )

        // TODO: For now this just delivers the data from the repo 1:1 into a Flow
        // TODO: We might add some business logic here if required
        // TODO: e.g. filter options (e.g. by category), etc.
    }
}
package org.qstuff.edc_app.ui.poioverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.qstuff.edc_app.domain.entity.ApiError
import org.qstuff.edc_app.domain.entity.Poi
import org.qstuff.edc_app.domain.usecase.GetNearbyPoiUseCase

/**
 * Hosts the UI state and requests and delivers the data
 * Also processes user interactions if they affect a UI state change
 */
class PoiOverviewViewModel(
    private val getNearbyPoiUseCase: GetNearbyPoiUseCase
): ViewModel() {

    companion object {
        private const val EVENLY_LOCATION_LAT = 52.500342
        private const val EVENLY_LOCATION_LNG = 13.425170
        private const val RADIUS_METER = 500
        // NOTE Using 1 here delivers an empty list, 50 is the hard max limit of the API
        private const val POI_RESULTS_MAX = 50
    }

    sealed class PoiUiState {
        data object Empty: PoiUiState()
        data object Loading: PoiUiState()
        class Loaded(val poiList: List<Poi>): PoiUiState()
        class Error(val message: String): PoiUiState()
    }

    private val _poiUiState: MutableStateFlow<PoiUiState> = MutableStateFlow(PoiUiState.Empty)
    val poiUiState: StateFlow<PoiUiState> = _poiUiState

    init {
        loadEvenlyNearbyPoi()
    }

    // For this demo we use the hardcoded values from above
    // For real life we should use the function below
    private fun loadEvenlyNearbyPoi() = loadNearbyPoi(
        Pair(EVENLY_LOCATION_LAT, EVENLY_LOCATION_LNG),
        RADIUS_METER,
        POI_RESULTS_MAX
    )

    fun loadNearbyPoi(
        locationLatLng: Pair<Double, Double>,
        radius: Int,
        limit: Int
    ) {
        viewModelScope.launch {

            _poiUiState.value = PoiUiState.Loading

            getNearbyPoiUseCase.execute(
                locationLatLng,
                radius,
                limit,
                ::setError
            ).collect {
                if (_poiUiState.value !is PoiUiState.Error)
                    _poiUiState.value = PoiUiState.Loaded(it)
            }
        }
    }

    private fun setError(error: ApiError) {

        // TODO: we can still evaluate the error.code here and decide how to handle it
        // TODO: For now we show the error.message on the screen

        _poiUiState.value = PoiUiState.Error("API Request Error:\n\nStatus: ${error.code}\n\n${error.message}")
    }
}
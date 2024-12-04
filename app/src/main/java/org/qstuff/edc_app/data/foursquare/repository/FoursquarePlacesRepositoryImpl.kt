package org.qstuff.edc_app.data.foursquare.repository

import com.google.gson.Gson
import org.qstuff.edc_app.data.foursquare.datasource.FoursquarePlacesDataSource
import org.qstuff.edc_app.domain.entity.ApiError
import org.qstuff.edc_app.domain.entity.Poi
import org.qstuff.edc_app.domain.repository.PoiRepository
import retrofit2.Response


/**
 * Retrieve data from the Foursquare API, checks for API errors and maps the result into
 * the required data format.
 * Signals errors in case.
 */
class FoursquarePlacesRepositoryImpl(
    private val dataSource: FoursquarePlacesDataSource
) : PoiRepository {

    /**
     * Get the POI nearby the location within radius
     */
    override suspend fun getNearbyPoi(
        locationLatLng: Pair<Double, Double>,
        radius: Int,
        limit: Int,
        setError: (ApiError) -> Unit
    ) : List<Poi> {

        return safeApiCall(
            apiCall = {
                dataSource.apiService.getNearbyPoi(
                    "${locationLatLng.first},${locationLatLng.second}",
                    radius.toString(),
                    limit.toString()
                )
            },
            onSuccess = { body ->
                body.results.map {
                    it.mapToPoi()
                }
            },
            onError = {
                setError(it)
                emptyList()
            }
        )
    }

    /**
     * This function performs the API call and does the error and exception checking.
     * You can easily use it for each API requests and each response type and handle
     * the results in the corresponding lambdas.
     */
    private suspend fun <T, R> safeApiCall(
        apiCall: suspend () -> Response<T>,
        onSuccess: (T) -> R,
        onError: (ApiError) -> R
    ): R {
        return try {

            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    onSuccess(body)
                } else {
                    val errorBodyString = "${response.errorBody()?.string()}"
                    val apiError = Gson().fromJson(errorBodyString, ApiError::class.java)
                    apiError.code = response.code()
                    onError(apiError)
                }
            } else {
                val errorBody = response.errorBody()?.string()
                onError(
                    ApiError(
                        message = errorBody ?: "Unknown error while API request",
                        code = response.code()
                    )
                )
            }
        } catch (e: Exception) {
            onError(
                ApiError(message = "Unknown Exception while API request: $e")
            )
        }
    }
}
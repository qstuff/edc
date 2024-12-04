package org.qstuff.edc_app.data.foursquare.repository

import com.google.gson.Gson
import org.qstuff.edc_app.data.foursquare.datasource.FoursquarePlacesDataSource
import org.qstuff.edc_app.domain.entity.ApiError
import org.qstuff.edc_app.domain.entity.Poi
import org.qstuff.edc_app.domain.repository.PoiRepository
import retrofit2.Response


/**
 * Retrieve data from the Foursquare API, check for API errors and map the result into
 * the required data format.
 * Signals errors in case using an error callback.
 */
class FoursquarePlacesRepositoryImpl(
    private val dataSource: FoursquarePlacesDataSource
) : PoiRepository {

    /**
     * Get the POI nearby the locationLatLng within radius
     *
     * TODO: it would also be ok to already return a Flow<List<Poi>> here and process the data
     * TODO: stream in the calling UseCase, but i did it like this to show that
     * TODO: this is also a possibility ;)
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
     * Http Status and possible error messages are evaluated and delivered to the caller for
     * further evaluation.
     *
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
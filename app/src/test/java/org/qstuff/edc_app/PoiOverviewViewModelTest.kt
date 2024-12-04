package org.qstuff.edc_app

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.qstuff.edc_app.data.foursquare.entity.FoursquareCategory
import org.qstuff.edc_app.data.foursquare.entity.FoursquareGeocodes
import org.qstuff.edc_app.data.foursquare.entity.FoursquareLocation
import org.qstuff.edc_app.data.foursquare.entity.FoursquareNearbyItem
import org.qstuff.edc_app.domain.usecase.GetNearbyPoiUseCase
import org.qstuff.edc_app.ui.poioverview.PoiOverviewViewModel
import kotlin.test.assertNotNull


@OptIn(ExperimentalCoroutinesApi::class)
class PoiOverviewViewModelTest : KoinTest {

    // TODO: move out testData to a common place so other tests can also use it
    private val testData = listOf(
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
        ).mapToPoi(),
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
        ).mapToPoi(),
    )

    private lateinit var mockGetNearbyPoiUseCase: GetNearbyPoiUseCase
    private lateinit var testPoiOverviewViewModel: PoiOverviewViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        startKoin {
            modules(
                module {
                    viewModel { PoiOverviewViewModel(get()) }
                    single { mockGetNearbyPoiUseCase }
                }
            )
        }

        mockGetNearbyPoiUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
        stopKoin()
    }

    @Test
    fun testViewModelInject() {
        testPoiOverviewViewModel = get()

        assertNotNull(testPoiOverviewViewModel)
    }

    /**
     * Test if the poiUiState in [PoiOverviewViewmodel] is set correctly to [PoiUiState.Loaded] when
     * [GetNearbyPoiUseCase.execute] returns a Flow with valid data.
     *
     * Also test if the data is passed correctly to [PoiUiState.Loaded]
     */
    @Test
    fun testLoadedStateFlowWithCollect() = runTest {

        coEvery {
            mockGetNearbyPoiUseCase.execute(
                eq(Pair(52.500342, 13.42517)),
                eq(500),
                eq(50),
                any()
            )
        } returns flow {
            emit(testData)
        }

        testPoiOverviewViewModel = get()

        testPoiOverviewViewModel.loadNearbyPoi(
            Pair(52.500342, 13.42517),
            500,
            50
        )

        assert(
            testPoiOverviewViewModel.poiUiState.value is
            PoiOverviewViewModel.PoiUiState.Loaded
        )

        assert(
           (testPoiOverviewViewModel.poiUiState.value as PoiOverviewViewModel.PoiUiState.Loaded).poiList == testData
        )
    }

    // TODO: test other poiUiStates
}
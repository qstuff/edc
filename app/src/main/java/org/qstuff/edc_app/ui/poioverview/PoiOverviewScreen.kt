package org.qstuff.edc_app.ui.poioverview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import org.qstuff.edc_app.domain.entity.Poi
import org.qstuff.edc_app.ui.common_composables.LoadingIndicator
import org.qstuff.edc_app.ui.mapview.OsmdroidScreen

/**
 * Presents a scrollable List of POI.
 * Overlays a Map View that shows a specific POI on the map.
 * Opens the system intent chooser for sharing the POI.
 *
 * Note: The design is very basic just for demonstration of functionality and should be beautified :)
 */
@Composable
fun PoiOverviewScreen(
    poiOverviewViewModel: PoiOverviewViewModel = koinViewModel<PoiOverviewViewModel>(),
    modifier: Modifier = Modifier,
    onSharePoi: (String) -> Unit
) {
    var uiState by remember { mutableStateOf(emptyList<Poi>()) }
    var showLoadingIndicator by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var mapVisible by remember { mutableStateOf(false) }

    // TODO: Evenly is the default, this needs to be changed
    var cameraPosition by remember { mutableStateOf(Pair(52.500342, 13.425170)) }

    when (val state = poiOverviewViewModel.poiUiState.collectAsState().value) {
        is PoiOverviewViewModel.PoiUiState.Empty -> {
            showLoadingIndicator = false
            showErrorMessage = false
        }
        is PoiOverviewViewModel.PoiUiState.Error -> {
            showLoadingIndicator = false
            showErrorMessage = true
            errorMessage = state.message
        }
        is PoiOverviewViewModel.PoiUiState.Loaded -> {
            showLoadingIndicator = false
            showErrorMessage = false
            uiState = state.poiList
        }
        PoiOverviewViewModel.PoiUiState.Loading -> {
            showLoadingIndicator = true
            showErrorMessage = false
        }
    }

    if (showLoadingIndicator)
        LoadingIndicator()
    else
        if (showErrorMessage)
            ErrorText(errorMessage)
        else {
            PoiList(
                modifier = modifier,
                poiList = uiState,
                onShowPoiOnMap = { poi ->
                    mapVisible = true
                    cameraPosition = Pair(
                        poi.mainLocationLat,
                        poi.mainLocationLong
                    )
                },
                onSharePoi = { onSharePoi(it.shareLink) }
            )
            AnimatedVisibility(
                visible = mapVisible,
                enter = slideInVertically(
                    initialOffsetY = { it }
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it }
                )
            ) {
                OsmdroidScreen (
                    cameraPosition = cameraPosition,
                    onCloseMap = {
                        mapVisible = false
                    }
                )
            }
        }
}

@Composable
fun PoiList(
    modifier: Modifier = Modifier,
    poiList: List<Poi> = listOf(),
    onShowPoiOnMap: (poi: Poi) -> Unit,
    onSharePoi: (poi: Poi) -> Unit
) {
    if (poiList.isEmpty())
        ErrorText(
            message = "No POI available"
        )
    else
        LazyColumn(
            modifier = modifier
        ) {
            items(poiList) { item ->
                PoiItem(
                    poi = item,
                    onShowPoiOnMap = onShowPoiOnMap,
                    onSharePoi = onSharePoi
                )
            }
        }
}

@Composable
fun PoiItem(
    poi: Poi,
    modifier: Modifier = Modifier,
    onShowPoiOnMap: (poi: Poi) -> Unit,
    onSharePoi: (poi: Poi) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = poi.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.W700,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Text(
            text = poi.category,
            fontWeight = FontWeight.W400,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            onClick = { onShowPoiOnMap(poi) }
        ) {
            Text(
                text = "Show this POI on Map"
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            onClick = { onSharePoi(poi) }
        ) {
            Text(
                text = "Share this POI with your friends"
            )
        }
    }
}

@Composable
fun ErrorText(
    message: String
) {
   Column(
       Modifier.fillMaxSize(),
       verticalArrangement = Arrangement.Center,
       horizontalAlignment = Alignment.CenterHorizontally
   ) {
       Text(
           text = message,
           fontWeight = FontWeight.W700,
           fontSize = 25.sp,
           modifier = Modifier
               .padding(24.dp)
               .align(alignment = Alignment.CenterHorizontally)
       )

       // TODO: we might add a "try again" Button or PullToRefresh
   }
}
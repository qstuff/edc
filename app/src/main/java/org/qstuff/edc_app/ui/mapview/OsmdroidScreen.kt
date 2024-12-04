package org.qstuff.edc_app.ui.mapview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


/**
 * Presents a map based on OpenStreetMap
 */
@Composable
fun OsmdroidScreen(
    cameraPosition: Pair<Double, Double>,
    zoom: Double = 18.0,
    onCloseMap: () -> Unit
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    // Cleanup when the Composable is removed
    DisposableEffect(Unit) {
        onDispose {
            mapView.onPause()
            mapView.onDetach()
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = {
            mapView.apply {
                setTileSource(TileSourceFactory.MAPNIK)

                controller.setZoom(zoom)
                controller.setCenter(
                    GeoPoint(
                        cameraPosition.first,
                        cameraPosition.second
                    )
                )

                val marker = Marker(this)
                marker.position = GeoPoint(
                    cameraPosition.first,
                    cameraPosition.second
                )
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                overlays.add(marker)
            }
        }
    )
    Button(
        onClick = onCloseMap,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 48.dp),
    ) {
        Text(
            text = "Close Map",
        )
    }
}
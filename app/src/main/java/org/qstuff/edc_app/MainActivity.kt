package org.qstuff.edc_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import org.osmdroid.config.Configuration
import org.qstuff.edc_app.ui.poioverview.PoiOverviewScreen
import org.qstuff.edc_app.ui.theme.EdcappTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupOsmDroid()

        enableEdgeToEdge()

        setContent {
            EdcappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PoiOverviewScreen(
                        modifier = Modifier.padding(innerPadding),
                        onSharePoi = { openShareIntent(it) }
                    )
                }
            }
        }
    }

    private fun openShareIntent(linkToShare: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, linkToShare)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share link with "))
    }

    private fun setupOsmDroid() {
        Configuration.getInstance()
            .userAgentValue = BuildConfig.APPLICATION_ID
    }
}


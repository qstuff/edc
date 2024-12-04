package org.qstuff.edc_app.ui.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.qstuff.edc_app.BuildConfig
import org.qstuff.edc_app.data.foursquare.datasource.FoursquarePlacesDataSource
import org.qstuff.edc_app.data.foursquare.repository.FoursquarePlacesRepositoryImpl
import org.qstuff.edc_app.domain.repository.PoiRepository


val dataModule = module {

    single {
        FoursquarePlacesDataSource(
            BuildConfig.FOURSQUARE_API_KEY,
            androidContext().cacheDir
        )
    }

    single<PoiRepository> { FoursquarePlacesRepositoryImpl(get()) }
}
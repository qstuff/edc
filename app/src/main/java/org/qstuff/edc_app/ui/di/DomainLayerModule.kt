package org.qstuff.edc_app.ui.di

import org.koin.dsl.module
import org.qstuff.edc_app.domain.usecase.GetNearbyPoiUseCase


val domainModule = module {

    factory { GetNearbyPoiUseCase(get()) }
}
package org.qstuff.edc_app.ui.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.qstuff.edc_app.ui.poioverview.PoiOverviewViewModel


val uiModule = module {

    viewModel { PoiOverviewViewModel(get()) }
}
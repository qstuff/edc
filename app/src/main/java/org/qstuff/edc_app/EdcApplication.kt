package org.qstuff.edc_app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.qstuff.edc_app.ui.di.dataModule
import org.qstuff.edc_app.ui.di.domainModule
import org.qstuff.edc_app.ui.di.uiModule


class EdcApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@EdcApplication)
            modules(
                uiModule,
                domainModule,
                dataModule
            )
        }
    }
}
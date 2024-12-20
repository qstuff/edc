## **E**venly **D**eveloper **C**hallenge App overview

A demo app that let the user browse some locations of POI around the [evenly](https://evenly.io) office.
The POI data is retrieved from the [Foursquare Places API](https://docs.foursquare.com/data-products/docs/place-delivery-options)
A POI can be shown on a Map (Open Street Maps is used)

The architecture follows clean architecture principles by dividing the code into
three layers (ui, domain, data). For now they all live in the app module, but they should
be moved into separate modules to provide a cleaner separation in case this app is
further developed.

It also uses the MVVM pattern using ViewModel which manages the ui state,
a usecase as a layer to the repository which contains the business logic,
a repository as an interface to the data source.

The app supports Android 11 and higher (minSDK 30, targetSDK 34)

## Build instructions

To use the Foursquare API you need to add a valid API token in local.properties:
foursquare.api.token=FOURSQUARE_API_TOKEN

## Tech Stack

* [Jetpack Compose](https://developer.android.com/develop/ui/compose/documentation)
* [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) and [Flow](https://kotlinlang.org/docs/flow.html)
* [Koin](https://insert-koin.io/) & [Koin Test](https://insert-koin.io/docs/quickstart/junit-test/) for dependency injection
* [Retrofit](https://square.github.io/retrofit/) and [OkHttp](https://square.github.io/okhttp/) for API requests
* [OSMDroid Map (Open Street Map)](https://github.com/osmdroid/osmdroid)
* [Mockk](https://mockk.io/) Mocking library for Kotlin

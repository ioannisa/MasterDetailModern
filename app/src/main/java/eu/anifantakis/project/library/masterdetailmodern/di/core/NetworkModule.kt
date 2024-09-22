package eu.anifantakis.project.library.masterdetailmodern.di.core

import eu.anifantakis.project.library.masterdetailmodern.BuildConfig
import eu.anifantakis.project.library.masterdetailmodern.core.data.networking.AuthHttpClient
import eu.anifantakis.project.library.masterdetailmodern.core.data.networking.MoviesHttpClient
import org.koin.dsl.module


val networkModule = module {
    single<AuthHttpClient> {
        AuthHttpClient(
            tag = "Auth",
            baseUrl = BuildConfig.BASE_URL_AUTH,
            persistManager = get()
        )
    }

    single<MoviesHttpClient> {
        MoviesHttpClient(
            tag = "Movies",
            baseUrl = BuildConfig.BASE_URL_MOVIES,
            apiKey = BuildConfig.API_KEY_MOVIES
        )
    }
}


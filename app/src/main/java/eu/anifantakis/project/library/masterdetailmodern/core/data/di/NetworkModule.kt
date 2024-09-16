package eu.anifantakis.project.library.masterdetailmodern.core.data.di

import eu.anifantakis.project.library.masterdetailmodern.BuildConfig
import eu.anifantakis.project.library.masterdetailmodern.core.data.networking.HttpClientFactory
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue
import org.koin.dsl.module

object AuthHttpClient : Qualifier {
    override val value: QualifierValue = "AuthHttpClient"
}

object MoviesHttpClient : Qualifier {
    override val value: QualifierValue = "MoviesHttpClient"
}

val networkModule = module {

    single(qualifier = AuthHttpClient) {
        HttpClientFactory().build(BuildConfig.BASE_URL_AUTH)
    }

    single(qualifier = MoviesHttpClient) {
        HttpClientFactory().build(BuildConfig.BASE_URL_MOVIES, BuildConfig.API_KEY_MOVIES)
    }
}


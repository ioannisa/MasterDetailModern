package eu.anifantakis.project.library.masterdetailmodern.core.data.di

import eu.anifantakis.lib.securepersist.PersistManager
import eu.anifantakis.project.library.masterdetailmodern.BuildConfig
import eu.anifantakis.project.library.masterdetailmodern.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<PersistManager> {
        PersistManager(androidContext(), "${BuildConfig.APPLICATION_ID}.securedPersistence")
    }
    viewModelOf(::MainViewModel)
}
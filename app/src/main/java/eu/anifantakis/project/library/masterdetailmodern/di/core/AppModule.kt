package eu.anifantakis.project.library.masterdetailmodern.di.core

import eu.anifantakis.lib.securepersist.PersistManager
import eu.anifantakis.project.library.masterdetailmodern.AppApplication
import eu.anifantakis.project.library.masterdetailmodern.BuildConfig
import eu.anifantakis.project.library.masterdetailmodern.MainViewModel
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.base.businesslogic.ObservableLoadingInteger
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.base.scaffold.ScaffoldViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<PersistManager> {
        PersistManager(androidContext(), "${BuildConfig.APPLICATION_ID}.securedPersistence")
    }
    viewModelOf(::MainViewModel)

    viewModelOf(::ScaffoldViewModel)
    single { ObservableLoadingInteger() }

    single<CoroutineScope> {
        (androidApplication() as AppApplication).applicationScope
    }
}
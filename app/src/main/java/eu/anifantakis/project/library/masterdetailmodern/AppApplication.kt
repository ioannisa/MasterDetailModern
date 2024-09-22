package eu.anifantakis.project.library.masterdetailmodern

import android.app.Application
import eu.anifantakis.project.library.masterdetailmodern.auth.data.di.authDataModule
import eu.anifantakis.project.library.masterdetailmodern.auth.presentation.di.authViewModelModule
import eu.anifantakis.project.library.masterdetailmodern.core.data.di.appModule
import eu.anifantakis.project.library.masterdetailmodern.database.dbModule
import eu.anifantakis.project.library.masterdetailmodern.core.data.di.networkModule
import eu.anifantakis.project.library.masterdetailmodern.movies.data.di.moviesDataModule
import eu.anifantakis.project.library.masterdetailmodern.movies.presentation.di.moviesViewModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class AppApplication: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@AppApplication)
            modules(
                appModule,

                dbModule,
                networkModule,

                authDataModule,
                authViewModelModule,

                moviesDataModule,
                moviesViewModule
            )
        }
    }
}
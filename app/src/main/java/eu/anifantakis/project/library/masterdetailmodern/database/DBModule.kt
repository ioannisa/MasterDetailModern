package eu.anifantakis.project.library.masterdetailmodern.database

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dbModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "app_database.db"
        ).build()
    }

    single { get<AppDatabase>().moviesDao }

}
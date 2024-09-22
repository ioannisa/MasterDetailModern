package eu.anifantakis.project.library.masterdetailmodern.database

import androidx.room.Database
import androidx.room.RoomDatabase
import eu.anifantakis.project.library.masterdetailmodern.database.dao.MoviesDao
import eu.anifantakis.project.library.masterdetailmodern.database.entity.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract val moviesDao: MoviesDao

}
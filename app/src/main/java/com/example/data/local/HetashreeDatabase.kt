package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.MockInitialData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        ArtistEntity::class,
        ProfileEntity::class,
        ArtistProfileEntity::class,
        ArtistPortfolioEntity::class,
        ArtistMeasurementsEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class HetashreeDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun artistDao(): ArtistDao
    abstract fun profileDao(): ProfileDao
    abstract fun artistProfileDao(): ArtistProfileDao
    abstract fun artistPortfolioDao(): ArtistPortfolioDao
    abstract fun artistMeasurementsDao(): ArtistMeasurementsDao

    companion object {
        @Volatile
        private var INSTANCE: HetashreeDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): HetashreeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HetashreeDatabase::class.java,
                    "hetashree_star_database"
                )
                    .addCallback(HetashreeDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class HetashreeDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialDatabase(database)
                }
            }
        }

        private suspend fun populateInitialDatabase(database: HetashreeDatabase) {
            val userDao = database.userDao()
            val profileDao = database.artistProfileDao()
            val portfolioDao = database.artistPortfolioDao()
            val measurementsDao = database.artistMeasurementsDao()

            // 1. Initial Users
            val initialUsers = listOf(
                UserEntity.fromDomain(MockInitialData.adminUser),
                UserEntity.fromDomain(MockInitialData.producerUser),
                UserEntity.fromDomain(MockInitialData.directorUser),
                UserEntity.fromDomain(MockInitialData.castingDirectorUser),
                UserEntity.fromDomain(MockInitialData.priyaUser),
                UserEntity.fromDomain(MockInitialData.aryanUser),
                UserEntity.fromDomain(MockInitialData.ananyaUser)
            )
            userDao.insertUsers(initialUsers)

            // 2. Initial Artist Profiles, Portfolios, and Measurements
            MockInitialData.initialProfiles.forEach { profile ->
                profileDao.insertProfile(ArtistProfileEntity.fromDomain(profile))
                portfolioDao.insertPortfolio(ArtistPortfolioEntity.fromDomain(profile.artistIdCode, profile.portfolio))
                measurementsDao.insertMeasurements(ArtistMeasurementsEntity.fromDomain(profile.artistIdCode, profile.measurements))
            }
        }
    }
}

package com.example.data.local

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * DatabaseProvider: A clean, thread-safe Room Database provider and factory module.
 * Provides singleton database and DAO instances to support dependency injection
 * and modular architecture across the Hetashree Star application.
 */
object DatabaseProvider {

    @Volatile
    private var databaseInstance: AppDatabase? = null

    @Volatile
    private var repositoryInstance: LocalDataRepository? = null

    /**
     * Provides the singleton AppDatabase instance.
     */
    fun provideDatabase(
        context: Context,
        scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    ): AppDatabase {
        return databaseInstance ?: synchronized(this) {
            databaseInstance ?: AppDatabase.getInstance(context, scope).also {
                databaseInstance = it
            }
        }
    }

    /**
     * Provides UserDao instance from AppDatabase.
     */
    fun provideUserDao(context: Context): UserDao {
        return provideDatabase(context).userDao()
    }

    /**
     * Provides ArtistDao instance from AppDatabase.
     */
    fun provideArtistDao(context: Context): ArtistDao {
        return provideDatabase(context).artistDao()
    }

    /**
     * Provides ProfileDao instance from AppDatabase.
     */
    fun provideProfileDao(context: Context): ProfileDao {
        return provideDatabase(context).profileDao()
    }

    /**
     * Provides ArtistProfileDao instance from AppDatabase.
     */
    fun provideArtistProfileDao(context: Context): ArtistProfileDao {
        return provideDatabase(context).artistProfileDao()
    }

    /**
     * Provides ArtistPortfolioDao instance from AppDatabase.
     */
    fun provideArtistPortfolioDao(context: Context): ArtistPortfolioDao {
        return provideDatabase(context).artistPortfolioDao()
    }

    /**
     * Provides ArtistMeasurementsDao instance from AppDatabase.
     */
    fun provideArtistMeasurementsDao(context: Context): ArtistMeasurementsDao {
        return provideDatabase(context).artistMeasurementsDao()
    }

    /**
     * Provides the singleton LocalDataRepository instance backed by Room DAOs.
     */
    fun provideLocalDataRepository(context: Context): LocalDataRepository {
        return repositoryInstance ?: synchronized(this) {
            repositoryInstance ?: run {
                val db = provideDatabase(context)
                LocalDataRepository(
                    userDao = db.userDao(),
                    artistDao = db.artistDao(),
                    profileDao = db.profileDao(),
                    artistProfileDao = db.artistProfileDao(),
                    artistPortfolioDao = db.artistPortfolioDao(),
                    artistMeasurementsDao = db.artistMeasurementsDao()
                ).also { repositoryInstance = it }
            }
        }
    }

    /**
     * Creates an in-memory AppDatabase instance for testing.
     */
    fun provideInMemoryDatabase(context: Context): AppDatabase {
        return AppDatabase.createInMemory(context)
    }

    /**
     * Clears cached singleton instances (useful during testing or logout).
     */
    fun clearCache() {
        synchronized(this) {
            databaseInstance = null
            repositoryInstance = null
        }
    }
}

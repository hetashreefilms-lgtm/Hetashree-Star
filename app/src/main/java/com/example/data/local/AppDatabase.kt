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

/**
 * AppDatabase: The primary Room Database for HETASHREE STAR.
 * Manages tables for Users, Artists, Profiles, Portfolios, and Measurements.
 */
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
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun artistDao(): ArtistDao
    abstract fun profileDao(): ProfileDao
    abstract fun artistProfileDao(): ArtistProfileDao
    abstract fun artistPortfolioDao(): ArtistPortfolioDao
    abstract fun artistMeasurementsDao(): ArtistMeasurementsDao

    companion object {
        const val DATABASE_NAME = "hetashree_star_database"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun createInMemory(context: Context): AppDatabase {
            return Room.inMemoryDatabaseBuilder(context.applicationContext, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        }
    }

    internal class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database)
                }
            }
        }

        private suspend fun populateInitialData(database: AppDatabase) {
            val userDao = database.userDao()
            val artistDao = database.artistDao()
            val profileDao = database.profileDao()
            val artistProfileDao = database.artistProfileDao()
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

            // 2. Initial Artists & Profiles
            MockInitialData.initialProfiles.forEach { profile ->
                artistProfileDao.insertProfile(ArtistProfileEntity.fromDomain(profile))
                portfolioDao.insertPortfolio(ArtistPortfolioEntity.fromDomain(profile.artistIdCode, profile.portfolio))
                measurementsDao.insertMeasurements(ArtistMeasurementsEntity.fromDomain(profile.artistIdCode, profile.measurements))

                val artistEntity = ArtistEntity(
                    artistIdCode = profile.artistIdCode,
                    userId = profile.userId,
                    stageName = profile.fullName,
                    primaryCategory = profile.primaryCategory,
                    allCategories = profile.allCategories,
                    profession = profile.profession,
                    actingExperienceYears = profile.actingExperienceYears,
                    modelingExperienceYears = profile.modelingExperienceYears,
                    registrationStatus = profile.verificationStatus.name,
                    isApproved = profile.isVerified,
                    isVerified = profile.isVerified,
                    registeredDate = "2026-11-12",
                    postsCount = profile.postsCount,
                    fansCount = profile.fansCount,
                    favesCount = profile.favesCount,
                    isChildArtist = profile.age < 18,
                    parentGuardianConsent = profile.parentGuardianConsent
                )
                artistDao.insertArtist(artistEntity)

                val profileEntity = ProfileEntity(
                    artistIdCode = profile.artistIdCode,
                    fullName = profile.fullName,
                    dateOfBirth = profile.dateOfBirth,
                    age = profile.age,
                    gender = profile.gender,
                    currentLocation = profile.currentLocation,
                    nativePlace = profile.nativePlace,
                    stateBelonging = profile.stateBelonging,
                    languages = profile.languages,
                    education = profile.education,
                    skills = profile.skills,
                    specialTalents = profile.specialTalents,
                    biography = profile.biography,
                    heightCm = profile.measurements.heightCm,
                    heightFeet = profile.measurements.heightFeet,
                    weightKg = profile.measurements.weightKg,
                    chestInches = profile.measurements.chestInches,
                    waistInches = profile.measurements.waistInches,
                    hipsInches = profile.measurements.hipsInches,
                    shoeSize = profile.measurements.shoeSize,
                    clothingSize = profile.measurements.clothingSize,
                    hairColor = profile.measurements.hairColor,
                    eyeColor = profile.measurements.eyeColor,
                    profilePhoto = profile.portfolio.profilePhoto,
                    closeUpPhoto = profile.portfolio.closeUpPhoto,
                    halfPhoto = profile.portfolio.halfPhoto,
                    fullLengthPhoto = profile.portfolio.fullLengthPhoto,
                    traditionalPhoto = profile.portfolio.traditionalPhoto,
                    westernPhoto = profile.portfolio.westernPhoto,
                    professionalPhoto = profile.portfolio.professionalPhoto,
                    modelingPhoto = profile.portfolio.modelingPhoto,
                    actingPhoto = profile.portfolio.actingPhoto,
                    privateMobile = profile.privateMobile,
                    privateEmail = profile.privateEmail,
                    privateAddress = profile.privateAddress,
                    emergencyContact = profile.emergencyContact
                )
                profileDao.insertProfile(profileEntity)
            }
        }
    }
}

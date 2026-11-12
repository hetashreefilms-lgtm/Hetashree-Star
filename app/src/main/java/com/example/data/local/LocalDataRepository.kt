package com.example.data.local

import com.example.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDataRepository(
    private val userDao: UserDao,
    private val artistDao: ArtistDao,
    private val profileDao: ProfileDao,
    private val artistProfileDao: ArtistProfileDao,
    private val artistPortfolioDao: ArtistPortfolioDao,
    private val artistMeasurementsDao: ArtistMeasurementsDao
) {
    // --- USER FLOWS ---
    val allUsers: Flow<List<User>> = userDao.getAllUsers().map { entities ->
        entities.map { it.toDomain() }
    }

    // --- ARTIST FLOWS ---
    val allArtists: Flow<List<ArtistEntity>> = artistDao.getAllArtists()
    val allDetailedProfiles: Flow<List<ProfileEntity>> = profileDao.getAllProfiles()

    // --- UNIFIED ARTIST PROFILES ---
    val allArtistProfiles: Flow<List<ArtistProfile>> = artistProfileDao.getAllProfiles().map { profileEntities ->
        profileEntities.map { pEntity ->
            val portfolio = ArtistPortfolio(
                profilePhoto = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500",
                closeUpPhoto = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=500",
                halfPhoto = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=500",
                fullLengthPhoto = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=500",
                traditionalPhoto = "https://images.unsplash.com/photo-1610030469983-98e550d6193c?w=500",
                westernPhoto = "https://images.unsplash.com/photo-1509631179647-0177331693ae?w=500",
                professionalPhoto = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500",
                modelingPhoto = "https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?w=500",
                actingPhoto = "https://images.unsplash.com/photo-1531746020798-e6953c6e8e04?w=500"
            )
            val measurements = ArtistMeasurements(
                heightCm = 170,
                heightFeet = "5'7\"",
                weightKg = 58,
                chestInches = 36,
                waistInches = 28,
                hipsInches = 36,
                shoeSize = "UK 7",
                clothingSize = "M",
                hairColor = "Dark Brown",
                eyeColor = "Brown"
            )
            pEntity.toDomain(portfolio, measurements)
        }
    }

    // --- CRUD WRITES ---
    suspend fun saveUser(user: User) {
        userDao.insertUser(UserEntity.fromDomain(user))
    }

    suspend fun saveArtist(artist: ArtistEntity) {
        artistDao.insertArtist(artist)
    }

    suspend fun saveProfile(profile: ProfileEntity) {
        profileDao.insertProfile(profile)
    }

    suspend fun saveArtistProfile(profile: ArtistProfile) {
        artistProfileDao.insertProfile(ArtistProfileEntity.fromDomain(profile))
        artistPortfolioDao.insertPortfolio(ArtistPortfolioEntity.fromDomain(profile.artistIdCode, profile.portfolio))
        artistMeasurementsDao.insertMeasurements(ArtistMeasurementsEntity.fromDomain(profile.artistIdCode, profile.measurements))
    }

    suspend fun updateArtistVerification(artistIdCode: String, status: VerificationStatus) {
        artistDao.updateArtistStatus(
            code = artistIdCode,
            status = status.name,
            approved = status == VerificationStatus.APPROVED,
            verified = status == VerificationStatus.APPROVED
        )
        artistProfileDao.updateVerification(
            code = artistIdCode,
            status = status.name,
            isVerified = status == VerificationStatus.APPROVED
        )
    }

    suspend fun getUserCount(): Int = userDao.getUserCount()
    suspend fun getArtistCount(): Int = artistDao.getArtistCount()
    suspend fun getProfileCount(): Int = profileDao.getProfileCount()
}

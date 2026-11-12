package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for User entity.
 * Provides full CRUD operations for managing application identities,
 * authentication states, session tokens, and role-based accounts.
 */
@Dao
interface UserDao {
    // --- CREATE ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    // --- READ ---
    @Query("SELECT * FROM users ORDER BY name ASC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserById(id: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun getUserByEmail(email: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    fun getUserByUsername(username: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE role = :role ORDER BY name ASC")
    fun getUsersByRole(role: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE verificationStatus = :status ORDER BY name ASC")
    fun getUsersByVerificationStatus(status: String): Flow<List<UserEntity>>

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int

    // --- UPDATE ---
    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET verificationStatus = :status, isVerified = :isVerified WHERE id = :id")
    suspend fun updateVerificationStatus(id: String, status: String, isVerified: Boolean)

    @Query("UPDATE users SET lastLoginTimestamp = :timestamp WHERE id = :id")
    suspend fun updateLastLogin(id: String, timestamp: Long)

    @Query("UPDATE users SET sessionToken = :token WHERE id = :id")
    suspend fun updateSessionToken(id: String, token: String?)

    // --- DELETE ---
    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUserById(id: String)

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}

/**
 * Data Access Object (DAO) for Artist entity.
 * Provides full CRUD operations for managing sequential Artist IDs (HT-ART-000001...),
 * professional categories, registration verification workflows, and fan/fave metrics.
 */
@Dao
interface ArtistDao {
    // --- CREATE ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtist(artist: ArtistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtists(artists: List<ArtistEntity>)

    // --- READ ---
    @Query("SELECT * FROM artists ORDER BY artistIdCode ASC")
    fun getAllArtists(): Flow<List<ArtistEntity>>

    @Query("SELECT * FROM artists WHERE artistIdCode = :code LIMIT 1")
    fun getArtistByCode(code: String): Flow<ArtistEntity?>

    @Query("SELECT * FROM artists WHERE userId = :userId LIMIT 1")
    fun getArtistByUserId(userId: String): Flow<ArtistEntity?>

    @Query("SELECT * FROM artists WHERE primaryCategory = :category ORDER BY artistIdCode ASC")
    fun getArtistsByCategory(category: String): Flow<List<ArtistEntity>>

    @Query("SELECT * FROM artists WHERE registrationStatus = :status ORDER BY artistIdCode ASC")
    fun getArtistsByStatus(status: String): Flow<List<ArtistEntity>>

    @Query("SELECT * FROM artists WHERE isVerified = :isVerified ORDER BY artistIdCode ASC")
    fun getArtistsByVerification(isVerified: Boolean): Flow<List<ArtistEntity>>

    @Query("SELECT * FROM artists WHERE isChildArtist = :isChild ORDER BY artistIdCode ASC")
    fun getArtistsByChildStatus(isChild: Boolean): Flow<List<ArtistEntity>>

    @Query("SELECT * FROM artists WHERE stageName LIKE '%' || :query || '%' OR artistIdCode LIKE '%' || :query || '%'")
    fun searchArtistsByStageName(query: String): Flow<List<ArtistEntity>>

    @Query("SELECT COUNT(*) FROM artists")
    suspend fun getArtistCount(): Int

    // --- UPDATE ---
    @Update
    suspend fun updateArtist(artist: ArtistEntity)

    @Query("UPDATE artists SET registrationStatus = :status, isApproved = :approved, isVerified = :verified WHERE artistIdCode = :code")
    suspend fun updateArtistStatus(code: String, status: String, approved: Boolean, verified: Boolean)

    @Query("UPDATE artists SET fansCount = :fans, favesCount = :faves, postsCount = :posts WHERE artistIdCode = :code")
    suspend fun updateFanMetrics(code: String, fans: Int, faves: Int, posts: Int)

    // --- DELETE ---
    @Delete
    suspend fun deleteArtist(artist: ArtistEntity)

    @Query("DELETE FROM artists WHERE artistIdCode = :code")
    suspend fun deleteArtistByCode(code: String)

    @Query("DELETE FROM artists")
    suspend fun deleteAllArtists()
}

/**
 * Data Access Object (DAO) for Profile entity.
 * Provides full CRUD operations for managing artist physical specs, demographics,
 * auto-calculated age, portfolio links, and shielded private contact details.
 */
@Dao
interface ProfileDao {
    // --- CREATE ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfiles(profiles: List<ProfileEntity>)

    // --- READ ---
    @Query("SELECT * FROM profiles WHERE artistIdCode = :code LIMIT 1")
    fun getProfileByCode(code: String): Flow<ProfileEntity?>

    @Query("SELECT * FROM profiles ORDER BY fullName ASC")
    fun getAllProfiles(): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM profiles WHERE gender = :gender ORDER BY fullName ASC")
    fun getProfilesByGender(gender: String): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM profiles WHERE currentLocation LIKE '%' || :city || '%' ORDER BY fullName ASC")
    fun getProfilesByLocation(city: String): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM profiles WHERE age BETWEEN :minAge AND :maxAge ORDER BY age ASC")
    fun getProfilesByAgeRange(minAge: Int, maxAge: Int): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM profiles WHERE fullName LIKE '%' || :query || '%' OR artistIdCode LIKE '%' || :query || '%' OR currentLocation LIKE '%' || :query || '%'")
    fun searchProfilesByNameOrLocation(query: String): Flow<List<ProfileEntity>>

    @Query("SELECT COUNT(*) FROM profiles")
    suspend fun getProfileCount(): Int

    // --- UPDATE ---
    @Update
    suspend fun updateProfile(profile: ProfileEntity)

    @Query("UPDATE profiles SET privateMobile = :mobile, privateEmail = :email, privateAddress = :address WHERE artistIdCode = :code")
    suspend fun updateContactInfo(code: String, mobile: String, email: String, address: String)

    @Query("UPDATE profiles SET biography = :bio WHERE artistIdCode = :code")
    suspend fun updateBiography(code: String, bio: String)

    // --- DELETE ---
    @Delete
    suspend fun deleteProfile(profile: ProfileEntity)

    @Query("DELETE FROM profiles WHERE artistIdCode = :code")
    suspend fun deleteProfileByCode(code: String)

    @Query("DELETE FROM profiles")
    suspend fun deleteAllProfiles()
}

/**
 * Data Access Object (DAO) for Unified ArtistProfileEntity.
 * Maintains backwards compatibility with LocalDataRepository.
 */
@Dao
interface ArtistProfileDao {
    @Query("SELECT * FROM artist_profiles ORDER BY artistIdCode ASC")
    fun getAllProfiles(): Flow<List<ArtistProfileEntity>>

    @Query("SELECT * FROM artist_profiles WHERE artistIdCode = :code LIMIT 1")
    fun getProfileByCode(code: String): Flow<ArtistProfileEntity?>

    @Query("SELECT * FROM artist_profiles WHERE primaryCategory = :category")
    fun getProfilesByCategory(category: String): Flow<List<ArtistProfileEntity>>

    @Query("SELECT * FROM artist_profiles WHERE verificationStatus = :status")
    fun getProfilesByVerificationStatus(status: String): Flow<List<ArtistProfileEntity>>

    @Query("SELECT * FROM artist_profiles WHERE fullName LIKE '%' || :query || '%' OR artistIdCode LIKE '%' || :query || '%'")
    fun searchProfiles(query: String): Flow<List<ArtistProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ArtistProfileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfiles(profiles: List<ArtistProfileEntity>)

    @Query("UPDATE artist_profiles SET verificationStatus = :status, isVerified = :isVerified WHERE artistIdCode = :code")
    suspend fun updateVerification(code: String, status: String, isVerified: Boolean)

    @Query("UPDATE artist_profiles SET fansCount = :fans WHERE artistIdCode = :code")
    suspend fun updateFansCount(code: String, fans: Int)

    @Query("DELETE FROM artist_profiles WHERE artistIdCode = :code")
    suspend fun deleteProfile(code: String)

    @Query("SELECT COUNT(*) FROM artist_profiles")
    suspend fun getProfileCount(): Int
}

/**
 * Data Access Object (DAO) for ArtistPortfolioEntity.
 */
@Dao
interface ArtistPortfolioDao {
    @Query("SELECT * FROM artist_portfolios WHERE artistIdCode = :code LIMIT 1")
    fun getPortfolioByCode(code: String): Flow<ArtistPortfolioEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortfolio(portfolio: ArtistPortfolioEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortfolios(portfolios: List<ArtistPortfolioEntity>)
}

/**
 * Data Access Object (DAO) for ArtistMeasurementsEntity.
 */
@Dao
interface ArtistMeasurementsDao {
    @Query("SELECT * FROM artist_measurements WHERE artistIdCode = :code LIMIT 1")
    fun getMeasurementsByCode(code: String): Flow<ArtistMeasurementsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurements(measurements: ArtistMeasurementsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurementsList(measurementsList: List<ArtistMeasurementsEntity>)
}

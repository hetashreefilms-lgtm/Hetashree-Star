package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    private lateinit var db: HetashreeDatabase
    private lateinit var userDao: UserDao
    private lateinit var artistDao: ArtistDao
    private lateinit var profileDao: ProfileDao
    private lateinit var artistProfileDao: ArtistProfileDao
    private lateinit var portfolioDao: ArtistPortfolioDao
    private lateinit var measurementsDao: ArtistMeasurementsDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, HetashreeDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = db.userDao()
        artistDao = db.artistDao()
        profileDao = db.profileDao()
        artistProfileDao = db.artistProfileDao()
        portfolioDao = db.artistPortfolioDao()
        measurementsDao = db.artistMeasurementsDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testAppNameResource() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("HETASHREE STAR", appName)
    }

    @Test
    fun testUserDaoCrudOperations() = runBlocking {
        // CREATE
        val user = UserEntity(
            id = "USR-TEST-01",
            email = "hetashreefilms@gmail.com",
            name = "HETASHREE Administration",
            username = "hetashree_official",
            role = "SUPER_ADMIN",
            avatarUrl = "https://example.com/admin.jpg",
            isVerified = true,
            companyName = "HETASHREE FILM'S PRODUCTION",
            authProvider = "EMAIL_PASSWORD",
            isEmailVerified = true,
            isActive = true
        )
        userDao.insertUser(user)

        // READ
        val byId = userDao.getUserById("USR-TEST-01").first()
        assertNotNull(byId)
        assertEquals("hetashreefilms@gmail.com", byId?.email)
        assertEquals(1, userDao.getUserCount())

        val byRole = userDao.getUsersByRole("SUPER_ADMIN").first()
        assertEquals(1, byRole.size)

        // UPDATE
        userDao.updateVerificationStatus("USR-TEST-01", "SUSPENDED", false)
        val updated = userDao.getUserById("USR-TEST-01").first()
        assertEquals("SUSPENDED", updated?.verificationStatus)
        assertFalse(updated?.isVerified ?: true)

        userDao.updateSessionToken("USR-TEST-01", "TOKEN_ABC_123")
        val updatedToken = userDao.getUserById("USR-TEST-01").first()
        assertEquals("TOKEN_ABC_123", updatedToken?.sessionToken)

        // DELETE
        userDao.deleteUserById("USR-TEST-01")
        val afterDelete = userDao.getUserById("USR-TEST-01").first()
        assertNull(afterDelete)
        assertEquals(0, userDao.getUserCount())
    }

    @Test
    fun testArtistDaoCrudOperations() = runBlocking {
        // CREATE
        val artist = ArtistEntity(
            artistIdCode = "HT-ART-000001",
            userId = "USR-ART-001",
            stageName = "Priya Sharma",
            primaryCategory = "Lead Artists",
            allCategories = listOf("Lead Artists", "Female Models", "Dancers"),
            profession = "Actress & Model",
            actingExperienceYears = 5,
            modelingExperienceYears = 6,
            registrationStatus = "PENDING",
            isApproved = false,
            isVerified = false,
            registeredDate = "2026-11-12",
            postsCount = 42,
            fansCount = 18400,
            favesCount = 312,
            isChildArtist = false,
            parentGuardianConsent = true
        )
        artistDao.insertArtist(artist)

        // READ
        val retrieved = artistDao.getArtistByCode("HT-ART-000001").first()
        assertNotNull(retrieved)
        assertEquals("Priya Sharma", retrieved?.stageName)
        assertEquals(1, artistDao.getArtistCount())

        val byCategory = artistDao.getArtistsByCategory("Lead Artists").first()
        assertEquals(1, byCategory.size)

        // UPDATE
        artistDao.updateArtistStatus("HT-ART-000001", "APPROVED", approved = true, verified = true)
        val approved = artistDao.getArtistByCode("HT-ART-000001").first()
        assertEquals("APPROVED", approved?.registrationStatus)
        assertTrue(approved?.isApproved ?: false)
        assertTrue(approved?.isVerified ?: false)

        artistDao.updateFanMetrics("HT-ART-000001", fans = 20000, faves = 350, posts = 45)
        val updatedMetrics = artistDao.getArtistByCode("HT-ART-000001").first()
        assertEquals(20000, updatedMetrics?.fansCount)
        assertEquals(350, updatedMetrics?.favesCount)
        assertEquals(45, updatedMetrics?.postsCount)

        // DELETE
        artistDao.deleteArtistByCode("HT-ART-000001")
        val afterDelete = artistDao.getArtistByCode("HT-ART-000001").first()
        assertNull(afterDelete)
        assertEquals(0, artistDao.getArtistCount())
    }

    @Test
    fun testProfileDaoCrudOperations() = runBlocking {
        // CREATE
        val profile = ProfileEntity(
            artistIdCode = "HT-ART-000001",
            fullName = "Priya Sharma",
            dateOfBirth = "1998-05-14",
            age = 28,
            gender = "Female",
            currentLocation = "Mumbai, Maharashtra",
            nativePlace = "Jaipur, Rajasthan",
            stateBelonging = "Maharashtra",
            languages = listOf("Hindi", "English", "Marwari"),
            education = "FTII Alumni",
            skills = listOf("Classical Kathak", "Horse Riding"),
            specialTalents = listOf("Singing"),
            biography = "Lead cinema actress.",
            heightCm = 172,
            heightFeet = "5'8\"",
            weightKg = 54,
            chestInches = 34,
            waistInches = 26,
            hipsInches = 36,
            shoeSize = "UK 6",
            clothingSize = "S / 36",
            hairColor = "Dark Brown",
            eyeColor = "Hazel",
            profilePhoto = "https://example.com/p1.jpg",
            closeUpPhoto = "https://example.com/p2.jpg",
            halfPhoto = "https://example.com/p3.jpg",
            fullLengthPhoto = "https://example.com/p4.jpg",
            traditionalPhoto = "https://example.com/p5.jpg",
            westernPhoto = "https://example.com/p6.jpg",
            professionalPhoto = "https://example.com/p7.jpg",
            modelingPhoto = "https://example.com/p8.jpg",
            actingPhoto = "https://example.com/p9.jpg",
            privateMobile = "+91 98200 45678",
            privateEmail = "priya.official.private@gmail.com",
            privateAddress = "B-402, Sea Green Heights, Bandra West, Mumbai",
            emergencyContact = "+91 98200 11223"
        )
        profileDao.insertProfile(profile)

        // READ & FILTER
        val retrieved = profileDao.getProfileByCode("HT-ART-000001").first()
        assertNotNull(retrieved)
        assertEquals("Priya Sharma", retrieved?.fullName)
        assertEquals(28, retrieved?.age)

        val byGender = profileDao.getProfilesByGender("Female").first()
        assertEquals(1, byGender.size)

        val byLocation = profileDao.getProfilesByLocation("Mumbai").first()
        assertEquals(1, byLocation.size)

        val byAgeRange = profileDao.getProfilesByAgeRange(25, 30).first()
        assertEquals(1, byAgeRange.size)

        // UPDATE
        profileDao.updateContactInfo("HT-ART-000001", "+91 98200 99999", "new.priya@gmail.com", "Andheri West, Mumbai")
        val updated = profileDao.getProfileByCode("HT-ART-000001").first()
        assertEquals("+91 98200 99999", updated?.privateMobile)
        assertEquals("new.priya@gmail.com", updated?.privateEmail)

        // DELETE
        profileDao.deleteProfileByCode("HT-ART-000001")
        val afterDelete = profileDao.getProfileByCode("HT-ART-000001").first()
        assertNull(afterDelete)
        assertEquals(0, profileDao.getProfileCount())
    }

    @Test
    fun testDatabaseProviderAndAppDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val inMemoryDb = DatabaseProvider.provideInMemoryDatabase(context)
        assertNotNull(inMemoryDb)
        assertNotNull(inMemoryDb.userDao())
        assertNotNull(inMemoryDb.artistDao())
        assertNotNull(inMemoryDb.profileDao())

        val singletonDb = DatabaseProvider.provideDatabase(context)
        assertNotNull(singletonDb)

        val userDaoFromProvider = DatabaseProvider.provideUserDao(context)
        assertNotNull(userDaoFromProvider)

        val localRepo = DatabaseProvider.provideLocalDataRepository(context)
        assertNotNull(localRepo)

        inMemoryDb.close()
    }
}

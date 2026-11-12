package com.example.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.model.*

class RoomConverters {
    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        return list?.joinToString("||") ?: ""
    }

    @TypeConverter
    fun toStringList(data: String?): List<String> {
        if (data.isNullOrBlank()) return emptyList()
        return data.split("||").filter { it.isNotBlank() }
    }
}

/**
 * Room Entity for User management and authentication state
 * Matches requirements from Part 3 & Part 4:
 * User roles: Artist, Model, Kids Artist, Producer, Director, Casting Director, Super Admin, Authorized Admin.
 */
@Entity(
    tableName = "users",
    indices = [
        Index(value = ["email"], unique = true),
        Index(value = ["username"], unique = true),
        Index(value = ["artistIdCode"], unique = false)
    ]
)
data class UserEntity(
    @PrimaryKey
    val id: String, // e.g. "USR-001"
    val email: String,
    val name: String,
    val username: String,
    val role: String, // UserRole enum name
    val avatarUrl: String,
    val isVerified: Boolean = false,
    val artistIdCode: String? = null, // e.g. "HT-ART-000001"
    val companyName: String? = null,
    val verificationStatus: String = "APPROVED", // PENDING, APPROVED, REJECTED, SUSPENDED

    // Authentication State & Session Details
    val authProvider: String = "EMAIL_PASSWORD", // EMAIL_PASSWORD, GOOGLE_ID
    val isEmailVerified: Boolean = true,
    val isActive: Boolean = true,
    val sessionToken: String? = null,
    val lastLoginTimestamp: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): User {
        val userRole = try {
            UserRole.valueOf(role)
        } catch (e: Exception) {
            UserRole.ARTIST
        }
        val vStatus = try {
            VerificationStatus.valueOf(verificationStatus)
        } catch (e: Exception) {
            VerificationStatus.APPROVED
        }
        return User(
            id = id,
            email = email,
            name = name,
            username = username,
            role = userRole,
            avatarUrl = avatarUrl,
            isVerified = isVerified,
            artistIdCode = artistIdCode,
            companyName = companyName,
            verificationStatus = vStatus
        )
    }

    companion object {
        fun fromDomain(user: User): UserEntity {
            return UserEntity(
                id = user.id,
                email = user.email,
                name = user.name,
                username = user.username,
                role = user.role.name,
                avatarUrl = user.avatarUrl,
                isVerified = user.isVerified,
                artistIdCode = user.artistIdCode,
                companyName = user.companyName,
                verificationStatus = user.verificationStatus.name
            )
        }
    }
}

/**
 * Room Entity for Artist identification, registration status, categories, and fan metrics.
 * Uses sequential unique Artist ID (HT-ART-000001, HT-ART-000002...).
 * Tracks FANS and FAVES metrics per specification.
 */
@Entity(
    tableName = "artists",
    indices = [
        Index(value = ["artistIdCode"], unique = true),
        Index(value = ["userId"], unique = true)
    ]
)
data class ArtistEntity(
    @PrimaryKey
    val artistIdCode: String, // HT-ART-000001, sequential unique ID
    val userId: String,       // Associated user identity
    val stageName: String,
    val primaryCategory: String, // e.g. Lead Artists, Female Models, Character Artists
    val allCategories: List<String>,
    val profession: String,
    val actingExperienceYears: Int = 0,
    val modelingExperienceYears: Int = 0,

    // Registration Status & Verification
    val registrationStatus: String = "PENDING", // PENDING, APPROVED, REJECTED, SUSPENDED
    val isApproved: Boolean = false,
    val isVerified: Boolean = false,
    val registeredDate: String = "",

    // Social Terminology (FANS instead of Followers, FAVES instead of Following)
    val postsCount: Int = 0,
    val fansCount: Int = 0,
    val favesCount: Int = 0,

    // Child Artist Compliance
    val isChildArtist: Boolean = false,
    val parentGuardianConsent: Boolean = false,
    val guardianName: String? = null,
    val guardianContact: String? = null
)

/**
 * Room Entity for complete Artist Profile details:
 * DOB & auto-calculated age, physical measurements, portfolio links, biography,
 * regional background, languages, and shielded private contact details.
 */
@Entity(
    tableName = "profiles",
    indices = [
        Index(value = ["artistIdCode"], unique = true)
    ]
)
data class ProfileEntity(
    @PrimaryKey
    val artistIdCode: String, // Matches ArtistEntity PK (e.g. HT-ART-000001)
    val fullName: String,
    val dateOfBirth: String,
    val age: Int,             // Auto-calculated from dateOfBirth
    val gender: String,       // Male, Female, Kid
    val currentLocation: String,
    val nativePlace: String,
    val stateBelonging: String,
    val languages: List<String>,
    val education: String,
    val skills: List<String>,
    val specialTalents: List<String>,
    val biography: String,

    // Physical Measurements
    val heightCm: Int,
    val heightFeet: String,
    val weightKg: Int,
    val chestInches: Int,
    val waistInches: Int,
    val hipsInches: Int,
    val shoeSize: String,
    val clothingSize: String,
    val hairColor: String,
    val eyeColor: String,

    // Portfolio Media
    val profilePhoto: String,
    val closeUpPhoto: String,
    val halfPhoto: String,
    val fullLengthPhoto: String,
    val traditionalPhoto: String,
    val westernPhoto: String,
    val professionalPhoto: String,
    val modelingPhoto: String,
    val actingPhoto: String,
    val introductionVideos: List<String> = emptyList(),
    val auditionVideos: List<String> = emptyList(),
    val youtubeLinks: List<String> = emptyList(),

    // Guarded Private Contact Information (Shielded from public view)
    val privateMobile: String,
    val privateEmail: String,
    val privateAddress: String,
    val emergencyContact: String
)

/**
 * ArtistProfileEntity: Unified entity used by LocalDataRepository and Room queries.
 */
@Entity(tableName = "artist_profiles")
data class ArtistProfileEntity(
    @PrimaryKey
    val artistIdCode: String, // e.g. HT-ART-000001
    val userId: String,
    val fullName: String,
    val username: String,
    val dateOfBirth: String,
    val age: Int,
    val gender: String,
    val primaryCategory: String,
    val allCategories: List<String>,
    val currentLocation: String,
    val nativePlace: String,
    val stateBelonging: String,
    val languages: List<String>,
    val education: String,
    val profession: String,
    val actingExperienceYears: Int,
    val modelingExperienceYears: Int,
    val skills: List<String>,
    val specialTalents: List<String>,
    val biography: String,
    val verificationStatus: String,
    val isVerified: Boolean = true,
    val postsCount: Int = 0,
    val fansCount: Int = 0,
    val favesCount: Int = 0,
    val privateMobile: String,
    val privateEmail: String,
    val privateAddress: String,
    val emergencyContact: String,
    val parentGuardianConsent: Boolean = false
) {
    fun toDomain(portfolio: ArtistPortfolio, measurements: ArtistMeasurements): ArtistProfile {
        val vStatus = try {
            VerificationStatus.valueOf(verificationStatus)
        } catch (e: Exception) {
            VerificationStatus.APPROVED
        }
        return ArtistProfile(
            artistIdCode = artistIdCode,
            userId = userId,
            fullName = fullName,
            username = username,
            dateOfBirth = dateOfBirth,
            age = age,
            gender = gender,
            primaryCategory = primaryCategory,
            allCategories = allCategories,
            currentLocation = currentLocation,
            nativePlace = nativePlace,
            stateBelonging = stateBelonging,
            languages = languages,
            education = education,
            profession = profession,
            actingExperienceYears = actingExperienceYears,
            modelingExperienceYears = modelingExperienceYears,
            skills = skills,
            specialTalents = specialTalents,
            biography = biography,
            portfolio = portfolio,
            measurements = measurements,
            verificationStatus = vStatus,
            isVerified = isVerified,
            postsCount = postsCount,
            fansCount = fansCount,
            favesCount = favesCount,
            privateMobile = privateMobile,
            privateEmail = privateEmail,
            privateAddress = privateAddress,
            emergencyContact = emergencyContact,
            parentGuardianConsent = parentGuardianConsent
        )
    }

    companion object {
        fun fromDomain(profile: ArtistProfile): ArtistProfileEntity {
            return ArtistProfileEntity(
                artistIdCode = profile.artistIdCode,
                userId = profile.userId,
                fullName = profile.fullName,
                username = profile.username,
                dateOfBirth = profile.dateOfBirth,
                age = profile.age,
                gender = profile.gender,
                primaryCategory = profile.primaryCategory,
                allCategories = profile.allCategories,
                currentLocation = profile.currentLocation,
                nativePlace = profile.nativePlace,
                stateBelonging = profile.stateBelonging,
                languages = profile.languages,
                education = profile.education,
                profession = profile.profession,
                actingExperienceYears = profile.actingExperienceYears,
                modelingExperienceYears = profile.modelingExperienceYears,
                skills = profile.skills,
                specialTalents = profile.specialTalents,
                biography = profile.biography,
                verificationStatus = profile.verificationStatus.name,
                isVerified = profile.isVerified,
                postsCount = profile.postsCount,
                fansCount = profile.fansCount,
                favesCount = profile.favesCount,
                privateMobile = profile.privateMobile,
                privateEmail = profile.privateEmail,
                privateAddress = profile.privateAddress,
                emergencyContact = profile.emergencyContact,
                parentGuardianConsent = profile.parentGuardianConsent
            )
        }
    }
}

@Entity(tableName = "artist_portfolios")
data class ArtistPortfolioEntity(
    @PrimaryKey
    val artistIdCode: String,
    val profilePhoto: String,
    val closeUpPhoto: String,
    val halfPhoto: String,
    val fullLengthPhoto: String,
    val traditionalPhoto: String,
    val westernPhoto: String,
    val professionalPhoto: String,
    val modelingPhoto: String,
    val actingPhoto: String,
    val introductionVideos: List<String> = emptyList(),
    val auditionVideos: List<String> = emptyList(),
    val youtubeLinks: List<String> = emptyList()
) {
    fun toDomain(): ArtistPortfolio {
        return ArtistPortfolio(
            profilePhoto = profilePhoto,
            closeUpPhoto = closeUpPhoto,
            halfPhoto = halfPhoto,
            fullLengthPhoto = fullLengthPhoto,
            traditionalPhoto = traditionalPhoto,
            westernPhoto = westernPhoto,
            professionalPhoto = professionalPhoto,
            modelingPhoto = modelingPhoto,
            actingPhoto = actingPhoto,
            introductionVideos = introductionVideos,
            auditionVideos = auditionVideos,
            youtubeLinks = youtubeLinks
        )
    }

    companion object {
        fun fromDomain(artistIdCode: String, portfolio: ArtistPortfolio): ArtistPortfolioEntity {
            return ArtistPortfolioEntity(
                artistIdCode = artistIdCode,
                profilePhoto = portfolio.profilePhoto,
                closeUpPhoto = portfolio.closeUpPhoto,
                halfPhoto = portfolio.halfPhoto,
                fullLengthPhoto = portfolio.fullLengthPhoto,
                traditionalPhoto = portfolio.traditionalPhoto,
                westernPhoto = portfolio.westernPhoto,
                professionalPhoto = portfolio.professionalPhoto,
                modelingPhoto = portfolio.modelingPhoto,
                actingPhoto = portfolio.actingPhoto,
                introductionVideos = portfolio.introductionVideos,
                auditionVideos = portfolio.auditionVideos,
                youtubeLinks = portfolio.youtubeLinks
            )
        }
    }
}

@Entity(tableName = "artist_measurements")
data class ArtistMeasurementsEntity(
    @PrimaryKey
    val artistIdCode: String,
    val heightCm: Int,
    val heightFeet: String,
    val weightKg: Int,
    val chestInches: Int,
    val waistInches: Int,
    val hipsInches: Int,
    val shoeSize: String,
    val clothingSize: String,
    val hairColor: String,
    val eyeColor: String
) {
    fun toDomain(): ArtistMeasurements {
        return ArtistMeasurements(
            heightCm = heightCm,
            heightFeet = heightFeet,
            weightKg = weightKg,
            chestInches = chestInches,
            waistInches = waistInches,
            hipsInches = hipsInches,
            shoeSize = shoeSize,
            clothingSize = clothingSize,
            hairColor = hairColor,
            eyeColor = eyeColor
        )
    }

    companion object {
        fun fromDomain(artistIdCode: String, measurements: ArtistMeasurements): ArtistMeasurementsEntity {
            return ArtistMeasurementsEntity(
                artistIdCode = artistIdCode,
                heightCm = measurements.heightCm,
                heightFeet = measurements.heightFeet,
                weightKg = measurements.weightKg,
                chestInches = measurements.chestInches,
                waistInches = measurements.waistInches,
                hipsInches = measurements.hipsInches,
                shoeSize = measurements.shoeSize,
                clothingSize = measurements.clothingSize,
                hairColor = measurements.hairColor,
                eyeColor = measurements.eyeColor
            )
        }
    }
}

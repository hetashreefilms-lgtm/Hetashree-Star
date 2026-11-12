package com.example

import com.example.data.HetashreeRepository
import com.example.data.MockInitialData
import com.example.model.UserRole
import com.example.model.VerificationStatus
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testCommissionCalculation() {
        val grossAmount = 50000.0
        val commissionPercent = 25.0
        val commissionAmount = (grossAmount * commissionPercent) / 100.0
        val artistPayable = grossAmount - commissionAmount

        assertEquals(12500.0, commissionAmount, 0.01)
        assertEquals(37500.0, artistPayable, 0.01)
    }

    @Test
    fun testArtistIdFormatAndRegistration() {
        val repo = HetashreeRepository.getInstance()
        val newProfile = repo.registerNewArtist(
            fullName = "Rhea Deshmukh",
            dob = "1999-03-22",
            calculatedAge = 27,
            gender = "Female",
            primaryCategory = "Lead Artists",
            currentLocation = "Mumbai, Maharashtra",
            nativePlace = "Nagpur",
            state = "Maharashtra",
            languages = listOf("Marathi", "Hindi", "English"),
            experienceYears = 4,
            skills = listOf("Classical Dance", "Method Acting"),
            bio = "Versatile lead actress.",
            mobile = "+91 9988776655",
            email = "rhea@talent.in",
            heightCm = 168,
            photoUrl = "https://example.com/photo.jpg"
        )

        assertTrue(newProfile.artistIdCode.startsWith("HT-ART-"))
        assertEquals(13, newProfile.artistIdCode.length)
        assertEquals("Rhea Deshmukh", newProfile.fullName)
        assertEquals(VerificationStatus.PENDING, newProfile.verificationStatus)
    }

    @Test
    fun testContactAccessPrivacyGuard() {
        val repo = HetashreeRepository.getInstance()
        // Producer should not automatically have access without approval
        repo.switchUser(MockInitialData.producerUser)
        val hasAccessBeforeApproval = repo.hasContactAccess("HT-ART-000001")
        assertFalse(hasAccessBeforeApproval)

        // Super Admin always has authorized access
        repo.switchUser(MockInitialData.adminUser)
        val adminHasAccess = repo.hasContactAccess("HT-ART-000001")
        assertTrue(adminHasAccess)
    }
}

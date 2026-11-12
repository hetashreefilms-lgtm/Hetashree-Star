package com.example.model

enum class UserRole(val displayName: String) {
    ARTIST("Artist"),
    MODEL("Model"),
    KIDS_ARTIST("Kids Artist"),
    PRODUCER("Producer"),
    DIRECTOR("Director"),
    CASTING_DIRECTOR("Casting Director"),
    SUPER_ADMIN("Super Admin"),
    AUTHORIZED_ADMIN("Authorized Admin")
}

enum class VerificationStatus {
    PENDING,
    APPROVED,
    REJECTED,
    SUSPENDED
}

data class User(
    val id: String,
    val email: String,
    val name: String,
    val username: String,
    val role: UserRole,
    val avatarUrl: String,
    val isVerified: Boolean = false,
    val artistIdCode: String? = null, // e.g. HT-ART-000001
    val companyName: String? = null,
    val verificationStatus: VerificationStatus = VerificationStatus.APPROVED
)

data class ArtistPortfolio(
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
)

data class ArtistMeasurements(
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
)

data class ArtistProfile(
    val artistIdCode: String, // HT-ART-000001
    val userId: String,
    val fullName: String,
    val username: String,
    val dateOfBirth: String,
    val age: Int,
    val gender: String, // Male, Female, Kid
    val primaryCategory: String, // e.g. "Lead Artist", "Female Model"
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
    val portfolio: ArtistPortfolio,
    val measurements: ArtistMeasurements,
    val verificationStatus: VerificationStatus = VerificationStatus.APPROVED,
    val isVerified: Boolean = true,
    // Social counters
    val postsCount: Int = 0,
    val fansCount: Int = 0,  // "FANS" instead of Followers
    val favesCount: Int = 0, // "FAVES" instead of Following
    // Private contact details (Locked for unauthorized users)
    val privateMobile: String,
    val privateEmail: String,
    val privateAddress: String,
    val emergencyContact: String,
    val parentGuardianConsent: Boolean = false
)

data class SocialPost(
    val id: String,
    val artistIdCode: String,
    val authorName: String,
    val authorUsername: String,
    val authorAvatarUrl: String,
    val isVerified: Boolean = true,
    val mediaUrl: String,
    val isVideo: Boolean = false,
    val caption: String,
    val hashtags: List<String> = emptyList(),
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val timeAgo: String = "Just now"
)

data class PostComment(
    val id: String,
    val postId: String,
    val authorName: String,
    val authorAvatarUrl: String,
    val authorRole: String,
    val text: String,
    val timeAgo: String = "Just now"
)

data class ReelVideo(
    val id: String,
    val artistIdCode: String,
    val authorName: String,
    val authorUsername: String,
    val authorAvatarUrl: String,
    val isVerified: Boolean = true,
    val videoThumbnailUrl: String,
    val caption: String,
    val hashtags: List<String>,
    val audioTitle: String,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false
)

data class StoryItem(
    val id: String,
    val artistIdCode: String,
    val authorName: String,
    val authorAvatarUrl: String,
    val mediaUrl: String,
    val caption: String = "",
    val timeAgo: String = "2h ago",
    val isViewed: Boolean = false
)

data class CastingCall(
    val id: String,
    val projectName: String,
    val projectType: String, // Films, Feature Films, Web Series, OTT, TV Serials, Ads, Music Videos, Fashion Shows
    val productionHouse: String,
    val roleName: String,
    val artistCategory: String,
    val gender: String,
    val minAge: Int,
    val maxAge: Int,
    val minHeightCm: Int,
    val maxHeightCm: Int,
    val location: String,
    val shootingDates: String,
    val workingDays: Int,
    val languages: List<String>,
    val experienceRequired: String,
    val requiredSkills: List<String>,
    val projectDescription: String,
    val budgetAmount: Double, // in INR
    val auditionDate: String,
    val auditionLocation: String,
    val applicationDeadline: String,
    val status: String = "Active", // Active, Closed
    val postedBy: String = "HETASHREE FILM'S PRODUCTION"
)

enum class ApplicationStatus(val label: String) {
    APPLIED("Applied"),
    UNDER_REVIEW("Under Review"),
    SHORTLISTED("Shortlisted"),
    SELECTED("Selected"),
    REJECTED("Rejected"),
    ON_HOLD("On Hold"),
    COMPLETED("Completed")
}

data class CastingApplication(
    val id: String,
    val castingCallId: String,
    val projectName: String,
    val roleName: String,
    val artistIdCode: String,
    val artistName: String,
    val artistAvatarUrl: String,
    val appliedDate: String,
    val status: ApplicationStatus = ApplicationStatus.APPLIED,
    val notes: String = ""
)

enum class ContactRequestStatus {
    PENDING,
    APPROVED,
    REJECTED,
    REVOKED
}

data class ContactAccessRequest(
    val id: String,
    val professionalUserId: String,
    val professionalName: String,
    val professionalRole: String,
    val professionalCompany: String,
    val artistIdCode: String,
    val artistName: String,
    val reason: String,
    val requestDate: String,
    val status: ContactRequestStatus = ContactRequestStatus.PENDING
)

enum class AgreementStatus(val label: String) {
    DRAFT("Draft"),
    PENDING("Pending"),
    SENT("Sent"),
    SIGNED("Signed"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    COMPLETED("Completed")
}

data class Agreement(
    val id: String, // e.g. HT-AGR-2026-001
    val producerName: String,
    val productionHouse: String,
    val projectName: String,
    val artistName: String,
    val artistIdCode: String,
    val projectCategory: String,
    val shootingLocation: String,
    val shootingDates: String,
    val workingDays: Int,
    val artistPayment: Double, // in INR e.g. 50000.0
    val agencyCommissionPercent: Double = 25.0, // default 25%
    val processingCharge: Double = 100.0, // ₹100 stamp/processing charge
    val paymentTerms: String = "50% Advance on signing, 50% upon completion of shoot schedule.",
    val cancellationTerms: String = "Mutual written notice of 7 business days required.",
    val confidentialityTerms: String = "Strict NDA applies. Script, footage, and costumes confidential.",
    val agreementDate: String,
    val status: AgreementStatus = AgreementStatus.PENDING,
    val signedByProducer: Boolean = false,
    val signedByArtist: Boolean = false,
    val verifiedByAdmin: Boolean = false
)

data class PaymentRecord(
    val id: String, // e.g. HT-PAY-8801
    val agreementId: String,
    val projectName: String,
    val producerName: String,
    val artistName: String,
    val artistIdCode: String,
    val producerPaymentAmount: Double,
    val agencyCommissionPercent: Double = 25.0,
    val agencyCommissionAmount: Double,
    val artistPayableAmount: Double,
    val advancePaid: Double = 0.0,
    val remainingAmount: Double,
    val paymentDate: String,
    val paymentMethod: String = "Bank Transfer (NEFT/RTGS)",
    val transactionRef: String,
    val status: String = "Completed", // Pending, Processing, Completed
    val receiptNotes: String = "Verified by HETASHREE Accounts Dept."
)

data class AvailabilityEntry(
    val id: String,
    val artistIdCode: String,
    val date: String, // YYYY-MM-DD
    val type: String, // Available, Audition, Shoot, Leave
    val note: String
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val messageText: String,
    val timestamp: String,
    val isRead: Boolean = true
)

data class ChatThread(
    val participantId: String,
    val participantName: String,
    val participantAvatar: String,
    val participantRole: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int = 0
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val type: String, // Casting, Social, Agreement, Payment, Contact
    val timeAgo: String,
    val isRead: Boolean = false
)

data class AuditLogEntry(
    val id: String,
    val timestamp: String,
    val actorName: String,
    val actorRole: String,
    val action: String,
    val details: String
)

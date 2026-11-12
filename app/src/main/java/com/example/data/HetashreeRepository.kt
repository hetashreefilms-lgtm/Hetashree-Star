package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HetashreeRepository private constructor() {

    companion object {
        @Volatile
        private var instance: HetashreeRepository? = null

        fun getInstance(): HetashreeRepository {
            return instance ?: synchronized(this) {
                instance ?: HetashreeRepository().also { instance = it }
            }
        }
    }

    // Active User
    private val _currentUser = MutableStateFlow<User>(MockInitialData.priyaUser)
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    // Artist Profiles
    private val _artistProfiles = MutableStateFlow<List<ArtistProfile>>(MockInitialData.initialProfiles)
    val artistProfiles: StateFlow<List<ArtistProfile>> = _artistProfiles.asStateFlow()

    // Stories
    private val _stories = MutableStateFlow<List<StoryItem>>(MockInitialData.initialStories)
    val stories: StateFlow<List<StoryItem>> = _stories.asStateFlow()

    // Posts
    private val _posts = MutableStateFlow<List<SocialPost>>(MockInitialData.initialPosts)
    val posts: StateFlow<List<SocialPost>> = _posts.asStateFlow()

    // Reels
    private val _reels = MutableStateFlow<List<ReelVideo>>(MockInitialData.initialReels)
    val reels: StateFlow<List<ReelVideo>> = _reels.asStateFlow()

    // Comments
    private val _comments = MutableStateFlow<List<PostComment>>(MockInitialData.initialComments)
    val comments: StateFlow<List<PostComment>> = _comments.asStateFlow()

    // Casting Calls
    private val _castingCalls = MutableStateFlow<List<CastingCall>>(MockInitialData.initialCastingCalls)
    val castingCalls: StateFlow<List<CastingCall>> = _castingCalls.asStateFlow()

    // Casting Applications
    private val _applications = MutableStateFlow<List<CastingApplication>>(MockInitialData.initialApplications)
    val applications: StateFlow<List<CastingApplication>> = _applications.asStateFlow()

    // Contact Requests
    private val _contactRequests = MutableStateFlow<List<ContactAccessRequest>>(MockInitialData.initialContactRequests)
    val contactRequests: StateFlow<List<ContactAccessRequest>> = _contactRequests.asStateFlow()

    // Agreements
    private val _agreements = MutableStateFlow<List<Agreement>>(MockInitialData.initialAgreements)
    val agreements: StateFlow<List<Agreement>> = _agreements.asStateFlow()

    // Payments
    private val _payments = MutableStateFlow<List<PaymentRecord>>(MockInitialData.initialPayments)
    val payments: StateFlow<List<PaymentRecord>> = _payments.asStateFlow()

    // Availability Calendar
    private val _availability = MutableStateFlow<List<AvailabilityEntry>>(MockInitialData.initialAvailability)
    val availability: StateFlow<List<AvailabilityEntry>> = _availability.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow<List<NotificationItem>>(MockInitialData.initialNotifications)
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // Chat Threads & Messages
    private val _chatThreads = MutableStateFlow<List<ChatThread>>(MockInitialData.initialChatThreads)
    val chatThreads: StateFlow<List<ChatThread>> = _chatThreads.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage("MSG-01", "USR-CAST-01", "USR-ART-001", "Hello Priya! Your monologue submission for Princess Maithili was extraordinary.", "10:30 AM"),
            ChatMessage("MSG-02", "USR-ART-001", "USR-CAST-01", "Thank you Sunaina ma'am! I have rehearsed the classical diction thoroughly.", "10:45 AM"),
            ChatMessage("MSG-03", "USR-CAST-01", "USR-ART-001", "Your monologue audition was phenomenal! Please send your close-up profile without makeup.", "11:20 AM")
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    // Audit Logs
    private val _auditLogs = MutableStateFlow<List<AuditLogEntry>>(MockInitialData.initialAuditLogs)
    val auditLogs: StateFlow<List<AuditLogEntry>> = _auditLogs.asStateFlow()

    // Next Artist ID Counter
    private var nextArtistIdNumber = 4

    private fun generateNextArtistId(): String {
        val id = String.format(Locale.US, "HT-ART-%06d", nextArtistIdNumber)
        nextArtistIdNumber++
        return id
    }

    private fun getCurrentTimestamp(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    }

    private fun logAction(action: String, details: String) {
        val user = _currentUser.value
        val log = AuditLogEntry(
            id = "LOG-${System.currentTimeMillis()}",
            timestamp = getCurrentTimestamp(),
            actorName = user.name,
            actorRole = user.role.name,
            action = action,
            details = details
        )
        _auditLogs.value = listOf(log) + _auditLogs.value
    }

    // Role Switching
    fun switchUser(user: User) {
        _currentUser.value = user
        logAction("SWITCH_USER", "Switched active user to ${user.name} (${user.role.displayName})")
    }

    // Post Interactions
    fun toggleLikePost(postId: String) {
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) {
                val newLiked = !post.isLiked
                val newCount = if (newLiked) post.likesCount + 1 else (post.likesCount - 1).coerceAtLeast(0)
                post.copy(isLiked = newLiked, likesCount = newCount)
            } else post
        }
    }

    fun toggleSavePost(postId: String) {
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) post.copy(isSaved = !post.isSaved) else post
        }
    }

    fun addComment(postId: String, text: String) {
        val user = _currentUser.value
        val newComment = PostComment(
            id = "CMT-${System.currentTimeMillis()}",
            postId = postId,
            authorName = user.name,
            authorAvatarUrl = user.avatarUrl,
            authorRole = user.role.displayName,
            text = text,
            timeAgo = "Just now"
        )
        _comments.value = _comments.value + newComment
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) post.copy(commentsCount = post.commentsCount + 1) else post
        }
    }

    // Reel Interactions
    fun toggleLikeReel(reelId: String) {
        _reels.value = _reels.value.map { reel ->
            if (reel.id == reelId) {
                val newLiked = !reel.isLiked
                val newCount = if (newLiked) reel.likesCount + 1 else (reel.likesCount - 1).coerceAtLeast(0)
                reel.copy(isLiked = newLiked, likesCount = newCount)
            } else reel
        }
    }

    fun toggleSaveReel(reelId: String) {
        _reels.value = _reels.value.map { reel ->
            if (reel.id == reelId) reel.copy(isSaved = !reel.isSaved) else reel
        }
    }

    // Create New Content
    fun createPost(mediaUrl: String, caption: String, hashtags: List<String>) {
        val user = _currentUser.value
        val artistId = user.artistIdCode ?: "HT-ART-000001"
        val newPost = SocialPost(
            id = "POST-${System.currentTimeMillis()}",
            artistIdCode = artistId,
            authorName = user.name,
            authorUsername = user.username,
            authorAvatarUrl = user.avatarUrl,
            isVerified = user.isVerified,
            mediaUrl = mediaUrl.ifBlank { "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=900" },
            isVideo = false,
            caption = caption,
            hashtags = hashtags,
            likesCount = 0,
            commentsCount = 0,
            sharesCount = 0,
            isLiked = false,
            isSaved = false,
            timeAgo = "Just now"
        )
        _posts.value = listOf(newPost) + _posts.value
        logAction("CREATE_POST", "Created social post with caption '${caption.take(30)}...'")
    }

    fun createReel(videoThumbUrl: String, caption: String, audioTrack: String, hashtags: List<String>) {
        val user = _currentUser.value
        val artistId = user.artistIdCode ?: "HT-ART-000001"
        val newReel = ReelVideo(
            id = "REEL-${System.currentTimeMillis()}",
            artistIdCode = artistId,
            authorName = user.name,
            authorUsername = user.username,
            authorAvatarUrl = user.avatarUrl,
            isVerified = user.isVerified,
            videoThumbnailUrl = videoThumbUrl.ifBlank { "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=900" },
            caption = caption,
            hashtags = hashtags,
            audioTitle = audioTrack.ifBlank { "HETASHREE Original Audition Beat" },
            likesCount = 0,
            commentsCount = 0,
            sharesCount = 0,
            isLiked = false,
            isSaved = false
        )
        _reels.value = listOf(newReel) + _reels.value
        logAction("CREATE_REEL", "Created Reel '${caption.take(30)}...'")
    }

    fun addStory(mediaUrl: String, caption: String) {
        val user = _currentUser.value
        val artistId = user.artistIdCode ?: "HT-ART-000001"
        val newStory = StoryItem(
            id = "STR-${System.currentTimeMillis()}",
            artistIdCode = artistId,
            authorName = user.name,
            authorAvatarUrl = user.avatarUrl,
            mediaUrl = mediaUrl.ifBlank { "https://images.unsplash.com/photo-1509631179647-0177331693ae?w=800" },
            caption = caption,
            timeAgo = "Just now",
            isViewed = false
        )
        _stories.value = listOf(newStory) + _stories.value
        logAction("ADD_STORY", "Added new story update")
    }

    // Artist Registration
    fun registerNewArtist(
        fullName: String,
        dob: String,
        calculatedAge: Int,
        gender: String,
        primaryCategory: String,
        currentLocation: String,
        nativePlace: String,
        state: String,
        languages: List<String>,
        experienceYears: Int,
        skills: List<String>,
        bio: String,
        mobile: String,
        email: String,
        heightCm: Int,
        photoUrl: String
    ): ArtistProfile {
        val newCode = generateNextArtistId()
        val username = fullName.lowercase().replace(" ", "_") + "_official"
        val newUser = User(
            id = "USR-${System.currentTimeMillis()}",
            email = email,
            name = fullName,
            username = username,
            role = if (calculatedAge < 18) UserRole.KIDS_ARTIST else UserRole.ARTIST,
            avatarUrl = photoUrl.ifBlank { "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500" },
            isVerified = false,
            artistIdCode = newCode,
            verificationStatus = VerificationStatus.PENDING
        )

        val heightFeet = "${heightCm / 30}'${((heightCm % 30) / 2.54).toInt()}\""

        val newProfile = ArtistProfile(
            artistIdCode = newCode,
            userId = newUser.id,
            fullName = fullName,
            username = username,
            dateOfBirth = dob,
            age = calculatedAge,
            gender = gender,
            primaryCategory = primaryCategory,
            allCategories = listOf(primaryCategory),
            currentLocation = currentLocation,
            nativePlace = nativePlace,
            stateBelonging = state,
            languages = languages,
            education = "Graduate / Dramatic Arts",
            profession = "Artist",
            actingExperienceYears = experienceYears,
            modelingExperienceYears = 0,
            skills = skills,
            specialTalents = listOf("Screen Acting", "Expressions"),
            biography = bio,
            portfolio = ArtistPortfolio(
                profilePhoto = newUser.avatarUrl,
                closeUpPhoto = newUser.avatarUrl,
                halfPhoto = newUser.avatarUrl,
                fullLengthPhoto = newUser.avatarUrl,
                traditionalPhoto = newUser.avatarUrl,
                westernPhoto = newUser.avatarUrl,
                professionalPhoto = newUser.avatarUrl,
                modelingPhoto = newUser.avatarUrl,
                actingPhoto = newUser.avatarUrl
            ),
            measurements = ArtistMeasurements(
                heightCm = heightCm,
                heightFeet = heightFeet,
                weightKg = 60,
                chestInches = 36,
                waistInches = 28,
                hipsInches = 36,
                shoeSize = "UK 7",
                clothingSize = "M",
                hairColor = "Black",
                eyeColor = "Black"
            ),
            verificationStatus = VerificationStatus.PENDING,
            isVerified = false,
            postsCount = 0,
            fansCount = 0,
            favesCount = 0,
            privateMobile = mobile,
            privateEmail = email,
            privateAddress = currentLocation,
            emergencyContact = mobile,
            parentGuardianConsent = calculatedAge < 18
        )

        _artistProfiles.value = _artistProfiles.value + newProfile
        _currentUser.value = newUser
        logAction("REGISTER_ARTIST", "Registered new artist $fullName with ID $newCode")
        return newProfile
    }

    // Artist Verification (Admin)
    fun updateArtistVerification(artistIdCode: String, status: VerificationStatus) {
        _artistProfiles.value = _artistProfiles.value.map { profile ->
            if (profile.artistIdCode == artistIdCode) {
                profile.copy(verificationStatus = status, isVerified = status == VerificationStatus.APPROVED)
            } else profile
        }
        logAction("VERIFY_ARTIST", "Updated verification for $artistIdCode to $status")
    }

    // Casting Calls & Applications
    fun applyToCastingCall(callId: String, notes: String = "") {
        val user = _currentUser.value
        val artistId = user.artistIdCode ?: "HT-ART-000001"
        val call = _castingCalls.value.find { it.id == callId } ?: return

        // Prevent duplicate
        val existing = _applications.value.find { it.castingCallId == callId && it.artistIdCode == artistId }
        if (existing != null) return

        val newApp = CastingApplication(
            id = "APP-${System.currentTimeMillis()}",
            castingCallId = callId,
            projectName = call.projectName,
            roleName = call.roleName,
            artistIdCode = artistId,
            artistName = user.name,
            artistAvatarUrl = user.avatarUrl,
            appliedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            status = ApplicationStatus.APPLIED,
            notes = notes
        )
        _applications.value = listOf(newApp) + _applications.value
        logAction("APPLY_CASTING", "Artist $artistId applied to '${call.projectName}' for role '${call.roleName}'")
    }

    fun updateApplicationStatus(applicationId: String, newStatus: ApplicationStatus) {
        _applications.value = _applications.value.map { app ->
            if (app.id == applicationId) {
                app.copy(status = newStatus)
            } else app
        }
        logAction("UPDATE_APPLICATION_STATUS", "Application $applicationId updated to ${newStatus.label}")
    }

    fun createCastingCall(call: CastingCall) {
        _castingCalls.value = listOf(call) + _castingCalls.value
        logAction("CREATE_CASTING_CALL", "Created casting call for '${call.projectName}' (${call.roleName})")
    }

    // Contact Access Requests
    fun requestContactAccess(artistIdCode: String, reason: String) {
        val user = _currentUser.value
        val artist = _artistProfiles.value.find { it.artistIdCode == artistIdCode } ?: return
        val newReq = ContactAccessRequest(
            id = "REQ-${System.currentTimeMillis()}",
            professionalUserId = user.id,
            professionalName = user.name,
            professionalRole = user.role.displayName,
            professionalCompany = user.companyName ?: "Production Guild",
            artistIdCode = artistIdCode,
            artistName = artist.fullName,
            reason = reason,
            requestDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            status = ContactRequestStatus.PENDING
        )
        _contactRequests.value = listOf(newReq) + _contactRequests.value
        logAction("REQUEST_CONTACT_ACCESS", "Requested private contact access for $artistIdCode by ${user.name}")
    }

    fun updateContactRequestStatus(requestId: String, status: ContactRequestStatus) {
        _contactRequests.value = _contactRequests.value.map { req ->
            if (req.id == requestId) req.copy(status = status) else req
        }
        logAction("UPDATE_CONTACT_ACCESS", "Contact request $requestId set to $status")
    }

    fun hasContactAccess(artistIdCode: String): Boolean {
        val user = _currentUser.value
        if (user.role == UserRole.SUPER_ADMIN || user.role == UserRole.AUTHORIZED_ADMIN) return true
        if (user.artistIdCode == artistIdCode) return true
        val approved = _contactRequests.value.any {
            it.artistIdCode == artistIdCode &&
                    it.professionalUserId == user.id &&
                    it.status == ContactRequestStatus.APPROVED
        }
        return approved
    }

    // Agreements
    fun createAgreementRequest(
        projectName: String,
        producerName: String,
        productionHouse: String,
        artistName: String,
        artistIdCode: String,
        projectCategory: String,
        location: String,
        dates: String,
        workingDays: Int,
        paymentAmount: Double
    ): Agreement {
        val id = "HT-AGR-2026-${String.format(Locale.US, "%03d", _agreements.value.size + 1)}"
        val newAgreement = Agreement(
            id = id,
            producerName = producerName,
            productionHouse = productionHouse,
            projectName = projectName,
            artistName = artistName,
            artistIdCode = artistIdCode,
            projectCategory = projectCategory,
            shootingLocation = location,
            shootingDates = dates,
            workingDays = workingDays,
            artistPayment = paymentAmount,
            agencyCommissionPercent = 25.0,
            processingCharge = 100.0,
            agreementDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            status = AgreementStatus.PENDING,
            signedByProducer = true
        )
        _agreements.value = listOf(newAgreement) + _agreements.value
        logAction("CREATE_AGREEMENT", "Created agreement $id for project '$projectName' (Charge: ₹100, Comm: 25%)")
        return newAgreement
    }

    fun signAgreementAsArtist(agreementId: String) {
        _agreements.value = _agreements.value.map { agr ->
            if (agr.id == agreementId) {
                agr.copy(
                    signedByArtist = true,
                    status = if (agr.signedByProducer) AgreementStatus.SIGNED else agr.status
                )
            } else agr
        }
        logAction("SIGN_AGREEMENT_ARTIST", "Artist signed agreement $agreementId")
    }

    fun approveAgreementByAdmin(agreementId: String) {
        _agreements.value = _agreements.value.map { agr ->
            if (agr.id == agreementId) {
                agr.copy(
                    verifiedByAdmin = true,
                    status = AgreementStatus.APPROVED
                )
            } else agr
        }
        logAction("APPROVE_AGREEMENT_ADMIN", "Admin verified & approved agreement $agreementId")
    }

    // Payments
    fun recordPayment(
        agreementId: String,
        projectName: String,
        producerName: String,
        artistName: String,
        artistIdCode: String,
        producerPayment: Double,
        commissionPercent: Double = 25.0,
        paymentMethod: String,
        transactionRef: String
    ): PaymentRecord {
        val commissionAmount = (producerPayment * commissionPercent) / 100.0
        val artistPayable = producerPayment - commissionAmount
        val record = PaymentRecord(
            id = "HT-PAY-${8800 + _payments.value.size + 1}",
            agreementId = agreementId,
            projectName = projectName,
            producerName = producerName,
            artistName = artistName,
            artistIdCode = artistIdCode,
            producerPaymentAmount = producerPayment,
            agencyCommissionPercent = commissionPercent,
            agencyCommissionAmount = commissionAmount,
            artistPayableAmount = artistPayable,
            advancePaid = artistPayable * 0.5,
            remainingAmount = artistPayable * 0.5,
            paymentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            paymentMethod = paymentMethod,
            transactionRef = transactionRef,
            status = "Completed",
            receiptNotes = "Producer funds cleared, 25% agency commission retained, advance transferred."
        )
        _payments.value = listOf(record) + _payments.value
        logAction("RECORD_PAYMENT", "Recorded payment for $projectName: Total ₹$producerPayment, Comm ₹$commissionAmount, Artist ₹$artistPayable")
        return record
    }

    // Messaging
    fun sendMessage(receiverId: String, text: String) {
        val user = _currentUser.value
        val newMsg = ChatMessage(
            id = "MSG-${System.currentTimeMillis()}",
            senderId = user.id,
            receiverId = receiverId,
            messageText = text,
            timestamp = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date()),
            isRead = true
        )
        _messages.value = _messages.value + newMsg
        logAction("SEND_MESSAGE", "Sent message to $receiverId")
    }

    // Calendar
    fun addAvailabilityEntry(entry: AvailabilityEntry) {
        _availability.value = _availability.value + entry
    }
}

package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.HetashreeRepository
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppDestination {
    SOCIAL_FEED,
    EXPLORE_DISCOVER,
    REELS_VIEWER,
    PROFILE_VIEW,
    CASTING_HUB,
    ARTIST_SEARCH,
    AGREEMENTS,
    PAYMENTS,
    CALENDAR,
    ADMIN_PORTAL,
    MESSAGES,
    NOTIFICATIONS,
    AUTH_AND_SWITCHER
}

class HetashreeViewModel : ViewModel() {

    private val repository = HetashreeRepository.getInstance()

    // Navigation State
    private val _currentDestination = MutableStateFlow(AppDestination.SOCIAL_FEED)
    val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

    // Selected items for detail view
    private val _selectedArtistIdCode = MutableStateFlow<String?>("HT-ART-000001")
    val selectedArtistIdCode: StateFlow<String?> = _selectedArtistIdCode.asStateFlow()

    private val _selectedCastingCall = MutableStateFlow<CastingCall?>(null)
    val selectedCastingCall: StateFlow<CastingCall?> = _selectedCastingCall.asStateFlow()

    // Interactive Dialogs & Modals
    private val _activeStory = MutableStateFlow<StoryItem?>(null)
    val activeStory: StateFlow<StoryItem?> = _activeStory.asStateFlow()

    private val _commentingPostId = MutableStateFlow<String?>(null)
    val commentingPostId: StateFlow<String?> = _commentingPostId.asStateFlow()

    private val _showCreateSheet = MutableStateFlow(false)
    val showCreateSheet: StateFlow<Boolean> = _showCreateSheet.asStateFlow()

    private val _contactRequestTarget = MutableStateFlow<ArtistProfile?>(null)
    val contactRequestTarget: StateFlow<ArtistProfile?> = _contactRequestTarget.asStateFlow()

    // Data streams from repository
    val currentUser = repository.currentUser.stateIn(viewModelScope, SharingStarted.Lazily, repository.currentUser.value)
    val artistProfiles = repository.artistProfiles.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val stories = repository.stories.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val posts = repository.posts.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val reels = repository.reels.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val comments = repository.comments.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val castingCalls = repository.castingCalls.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val applications = repository.applications.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val contactRequests = repository.contactRequests.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val agreements = repository.agreements.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val payments = repository.payments.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val availability = repository.availability.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val notifications = repository.notifications.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val chatThreads = repository.chatThreads.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val messages = repository.messages.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val auditLogs = repository.auditLogs.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Navigation Actions
    fun navigateTo(destination: AppDestination) {
        _currentDestination.value = destination
    }

    fun openArtistProfile(artistIdCode: String) {
        _selectedArtistIdCode.value = artistIdCode
        _currentDestination.value = AppDestination.PROFILE_VIEW
    }

    fun openCastingDetail(call: CastingCall) {
        _selectedCastingCall.value = call
        _currentDestination.value = AppDestination.CASTING_HUB
    }

    // Story Actions
    fun openStory(story: StoryItem) {
        _activeStory.value = story
    }

    fun closeStory() {
        _activeStory.value = null
    }

    // Comment Actions
    fun openComments(postId: String) {
        _commentingPostId.value = postId
    }

    fun closeComments() {
        _commentingPostId.value = null
    }

    fun addComment(postId: String, text: String) {
        if (text.isNotBlank()) {
            repository.addComment(postId, text.trim())
        }
    }

    // Create Sheet
    fun showCreateModal(show: Boolean) {
        _showCreateSheet.value = show
    }

    // Contact Access Dialog
    fun promptContactRequest(profile: ArtistProfile) {
        _contactRequestTarget.value = profile
    }

    fun dismissContactRequest() {
        _contactRequestTarget.value = null
    }

    fun submitContactRequest(artistIdCode: String, reason: String) {
        repository.requestContactAccess(artistIdCode, reason)
        _contactRequestTarget.value = null
    }

    fun hasContactAccess(artistIdCode: String): Boolean {
        return repository.hasContactAccess(artistIdCode)
    }

    // Post / Reel actions
    fun toggleLikePost(postId: String) = repository.toggleLikePost(postId)
    fun toggleSavePost(postId: String) = repository.toggleSavePost(postId)
    fun toggleLikeReel(reelId: String) = repository.toggleLikeReel(reelId)
    fun toggleSaveReel(reelId: String) = repository.toggleSaveReel(reelId)

    // User Switch
    fun switchUser(user: User) {
        repository.switchUser(user)
    }

    // Casting
    fun applyToCasting(callId: String, notes: String = "") {
        repository.applyToCastingCall(callId, notes)
    }

    fun updateApplicationStatus(appId: String, status: ApplicationStatus) {
        repository.updateApplicationStatus(appId, status)
    }

    fun createCastingCall(call: CastingCall) {
        repository.createCastingCall(call)
    }

    // Agreement & Payments
    fun signAgreementAsArtist(agreementId: String) {
        repository.signAgreementAsArtist(agreementId)
    }

    fun approveAgreementByAdmin(agreementId: String) {
        repository.approveAgreementByAdmin(agreementId)
    }

    fun createAgreement(
        projectName: String,
        producerName: String,
        productionHouse: String,
        artistName: String,
        artistIdCode: String,
        category: String,
        location: String,
        dates: String,
        days: Int,
        amount: Double
    ) {
        repository.createAgreementRequest(
            projectName, producerName, productionHouse, artistName, artistIdCode,
            category, location, dates, days, amount
        )
    }

    fun recordPayment(
        agreementId: String,
        projectName: String,
        producerName: String,
        artistName: String,
        artistIdCode: String,
        producerPayment: Double,
        commissionPercent: Double = 25.0,
        method: String,
        ref: String
    ) {
        repository.recordPayment(
            agreementId, projectName, producerName, artistName, artistIdCode,
            producerPayment, commissionPercent, method, ref
        )
    }

    // Artist Verification
    fun updateArtistVerification(artistIdCode: String, status: VerificationStatus) {
        repository.updateArtistVerification(artistIdCode, status)
    }

    // Registration
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
    ) {
        val newProfile = repository.registerNewArtist(
            fullName, dob, calculatedAge, gender, primaryCategory,
            currentLocation, nativePlace, state, languages, experienceYears,
            skills, bio, mobile, email, heightCm, photoUrl
        )
        _selectedArtistIdCode.value = newProfile.artistIdCode
        _currentDestination.value = AppDestination.PROFILE_VIEW
    }

    // Content Creation
    fun createPost(mediaUrl: String, caption: String, hashtags: List<String>) {
        repository.createPost(mediaUrl, caption, hashtags)
    }

    fun createReel(thumbUrl: String, caption: String, audio: String, hashtags: List<String>) {
        repository.createReel(thumbUrl, caption, audio, hashtags)
    }

    fun addStory(mediaUrl: String, caption: String) {
        repository.addStory(mediaUrl, caption)
    }

    // Calendar
    fun addAvailabilityEntry(entry: AvailabilityEntry) {
        repository.addAvailabilityEntry(entry)
    }

    // Chat
    fun sendMessage(receiverId: String, text: String) {
        repository.sendMessage(receiverId, text)
    }
}

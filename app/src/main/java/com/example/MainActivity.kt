package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.HetashreeBlack
import com.example.ui.theme.HetashreeStarTheme
import com.example.viewmodel.AppDestination
import com.example.viewmodel.HetashreeViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: HetashreeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HetashreeStarTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: HetashreeViewModel) {
    val destination by viewModel.currentDestination.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val profiles by viewModel.artistProfiles.collectAsState()
    val stories by viewModel.stories.collectAsState()
    val posts by viewModel.posts.collectAsState()
    val reels by viewModel.reels.collectAsState()
    val comments by viewModel.comments.collectAsState()
    val castingCalls by viewModel.castingCalls.collectAsState()
    val applications by viewModel.applications.collectAsState()
    val contactRequests by viewModel.contactRequests.collectAsState()
    val agreements by viewModel.agreements.collectAsState()
    val payments by viewModel.payments.collectAsState()
    val availability by viewModel.availability.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val chatThreads by viewModel.chatThreads.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val auditLogs by viewModel.auditLogs.collectAsState()

    val selectedArtistIdCode by viewModel.selectedArtistIdCode.collectAsState()
    val activeStory by viewModel.activeStory.collectAsState()
    val commentingPostId by viewModel.commentingPostId.collectAsState()
    val showCreateSheet by viewModel.showCreateSheet.collectAsState()
    val contactRequestTarget by viewModel.contactRequestTarget.collectAsState()

    // Back handler for secondary screens
    BackHandler(enabled = destination != AppDestination.SOCIAL_FEED) {
        when (destination) {
            AppDestination.REELS_VIEWER,
            AppDestination.EXPLORE_DISCOVER,
            AppDestination.PROFILE_VIEW -> viewModel.navigateTo(AppDestination.SOCIAL_FEED)
            AppDestination.ARTIST_SEARCH,
            AppDestination.AGREEMENTS,
            AppDestination.PAYMENTS,
            AppDestination.CALENDAR -> viewModel.navigateTo(AppDestination.CASTING_HUB)
            AppDestination.ADMIN_PORTAL -> viewModel.navigateTo(AppDestination.CASTING_HUB)
            AppDestination.MESSAGES,
            AppDestination.NOTIFICATIONS,
            AppDestination.AUTH_AND_SWITCHER -> viewModel.navigateTo(AppDestination.SOCIAL_FEED)
            AppDestination.CASTING_HUB -> viewModel.navigateTo(AppDestination.SOCIAL_FEED)
            else -> viewModel.navigateTo(AppDestination.SOCIAL_FEED)
        }
    }

    // Determine whether to show top bar and bottom nav
    val isImmersiveReels = destination == AppDestination.REELS_VIEWER

    Scaffold(
        modifier = Modifier.fillMaxSize().background(HetashreeBlack),
        containerColor = HetashreeBlack,
        topBar = {
            if (!isImmersiveReels) {
                HetashreeTopBar(
                    currentUser = currentUser,
                    currentDestination = destination,
                    unreadNotifications = notifications.count { !it.isRead },
                    unreadMessages = chatThreads.sumOf { it.unreadCount },
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        },
        bottomBar = {
            if (!isImmersiveReels) {
                HetashreeBottomNav(
                    currentDestination = destination,
                    onNavigate = { viewModel.navigateTo(it) },
                    onCreateClick = { viewModel.showCreateModal(true) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isImmersiveReels) androidx.compose.foundation.layout.PaddingValues() else innerPadding)
                .background(HetashreeBlack)
        ) {
            when (destination) {
                AppDestination.SOCIAL_FEED -> {
                    HomeScreen(
                        currentUser = currentUser,
                        stories = stories,
                        posts = posts,
                        onStoryClick = { viewModel.openStory(it) },
                        onAddStoryClick = { viewModel.showCreateModal(true) },
                        onArtistClick = { viewModel.openArtistProfile(it) },
                        onLikePost = { viewModel.toggleLikePost(it) },
                        onCommentClick = { viewModel.openComments(it) },
                        onSavePost = { viewModel.toggleSavePost(it) }
                    )
                }

                AppDestination.EXPLORE_DISCOVER -> {
                    DiscoverScreen(
                        profiles = profiles,
                        posts = posts,
                        reels = reels,
                        onArtistClick = { viewModel.openArtistProfile(it) },
                        onNavigateToSearch = { viewModel.navigateTo(AppDestination.ARTIST_SEARCH) },
                        onOpenReels = { viewModel.navigateTo(AppDestination.REELS_VIEWER) }
                    )
                }

                AppDestination.REELS_VIEWER -> {
                    ReelsScreen(
                        reels = reels,
                        onBack = { viewModel.navigateTo(AppDestination.SOCIAL_FEED) },
                        onArtistClick = { viewModel.openArtistProfile(it) },
                        onLikeReel = { viewModel.toggleLikeReel(it) },
                        onCommentClick = { viewModel.openComments(it) },
                        onSaveReel = { viewModel.toggleSaveReel(it) }
                    )
                }

                AppDestination.PROFILE_VIEW -> {
                    val targetCode = selectedArtistIdCode ?: currentUser.artistIdCode ?: "HT-ART-000001"
                    val profile = profiles.find { it.artistIdCode == targetCode } ?: profiles.first()
                    val hasAccess = viewModel.hasContactAccess(profile.artistIdCode)

                    ProfileScreen(
                        profile = profile,
                        currentUser = currentUser,
                        hasContactAccess = hasAccess,
                        posts = posts,
                        reels = reels,
                        onRequestContactAccess = { viewModel.promptContactRequest(profile) },
                        onMessageClick = { viewModel.navigateTo(AppDestination.MESSAGES) },
                        onOpenReels = { viewModel.navigateTo(AppDestination.REELS_VIEWER) }
                    )
                }

                AppDestination.CASTING_HUB -> {
                    CastingHubScreen(
                        currentUser = currentUser,
                        castingCalls = castingCalls,
                        applications = applications,
                        onApplyToCall = { viewModel.applyToCasting(it) },
                        onNavigate = { viewModel.navigateTo(it) },
                        onUpdateApplicationStatus = { id, status -> viewModel.updateApplicationStatus(id, status) }
                    )
                }

                AppDestination.ARTIST_SEARCH -> {
                    ArtistSearchScreen(
                        profiles = profiles,
                        onBack = { viewModel.navigateTo(AppDestination.CASTING_HUB) },
                        onArtistSelect = { viewModel.openArtistProfile(it) },
                        onRequestContactAccess = { viewModel.promptContactRequest(it) }
                    )
                }

                AppDestination.AGREEMENTS -> {
                    AgreementsScreen(
                        currentUser = currentUser,
                        agreements = agreements,
                        onBack = { viewModel.navigateTo(AppDestination.CASTING_HUB) },
                        onSignAsArtist = { viewModel.signAgreementAsArtist(it) },
                        onApproveByAdmin = { viewModel.approveAgreementByAdmin(it) }
                    )
                }

                AppDestination.PAYMENTS -> {
                    PaymentsScreen(
                        currentUser = currentUser,
                        payments = payments,
                        onBack = { viewModel.navigateTo(AppDestination.CASTING_HUB) },
                        onRecordNewPayment = { agrId, proj, prod, art, code, amount, comm, meth, ref ->
                            viewModel.recordPayment(agrId, proj, prod, art, code, amount, comm, meth, ref)
                        }
                    )
                }

                AppDestination.CALENDAR -> {
                    AvailabilityCalendarScreen(
                        currentUser = currentUser,
                        availability = availability,
                        onBack = { viewModel.navigateTo(AppDestination.CASTING_HUB) },
                        onAddEntry = { viewModel.addAvailabilityEntry(it) }
                    )
                }

                AppDestination.ADMIN_PORTAL -> {
                    AdminDashboardScreen(
                        profiles = profiles,
                        castingCalls = castingCalls,
                        applications = applications,
                        agreements = agreements,
                        payments = payments,
                        contactRequests = contactRequests,
                        auditLogs = auditLogs,
                        onBack = { viewModel.navigateTo(AppDestination.CASTING_HUB) },
                        onVerifyArtist = { code, status -> viewModel.updateArtistVerification(code, status) },
                        onApproveContactRequest = { id, status -> viewModel.submitContactRequest(id, status.name) }
                    )
                }

                AppDestination.MESSAGES -> {
                    MessagesScreen(
                        currentUser = currentUser,
                        threads = chatThreads,
                        messages = messages,
                        onBack = { viewModel.navigateTo(AppDestination.SOCIAL_FEED) },
                        onSendMessage = { receiverId, text -> viewModel.sendMessage(receiverId, text) }
                    )
                }

                AppDestination.NOTIFICATIONS -> {
                    NotificationsScreen(
                        notifications = notifications,
                        onBack = { viewModel.navigateTo(AppDestination.SOCIAL_FEED) }
                    )
                }

                AppDestination.AUTH_AND_SWITCHER -> {
                    AuthAndRoleScreen(
                        currentUser = currentUser,
                        onBack = { viewModel.navigateTo(AppDestination.SOCIAL_FEED) },
                        onSwitchUser = {
                            viewModel.switchUser(it)
                            viewModel.navigateTo(AppDestination.SOCIAL_FEED)
                        },
                        onRegisterArtist = { name, dob, age, gen, cat, loc, nat, st, langs, exp, sks, bio, mob, eml, ht, pht ->
                            viewModel.registerNewArtist(name, dob, age, gen, cat, loc, nat, st, langs, exp, sks, bio, mob, eml, ht, pht)
                        }
                    )
                }
            }
        }
    }

    // Story Dialog
    activeStory?.let { story ->
        StoryViewerDialog(
            story = story,
            onDismiss = { viewModel.closeStory() },
            onNext = {
                val nextStory = stories.getOrNull(stories.indexOf(story) + 1)
                if (nextStory != null) viewModel.openStory(nextStory) else viewModel.closeStory()
            },
            onPrevious = {
                val prevStory = stories.getOrNull(stories.indexOf(story) - 1)
                if (prevStory != null) viewModel.openStory(prevStory) else viewModel.closeStory()
            }
        )
    }

    // Comments Sheet
    commentingPostId?.let { postId ->
        CommentsBottomSheet(
            postId = postId,
            comments = comments,
            onDismiss = { viewModel.closeComments() },
            onAddComment = { text -> viewModel.addComment(postId, text) }
        )
    }

    // Create Media Bottom Sheet
    if (showCreateSheet) {
        CreateMediaBottomSheet(
            currentUser = currentUser,
            onDismiss = { viewModel.showCreateModal(false) },
            onCreatePost = { url, cap, tags -> viewModel.createPost(url, cap, tags) },
            onCreateReel = { thumb, cap, audio, tags -> viewModel.createReel(thumb, cap, audio, tags) },
            onAddStory = { url, cap -> viewModel.addStory(url, cap) },
            onCreateCastingCall = { call -> viewModel.createCastingCall(call) }
        )
    }

    // Contact Access Request Dialog
    contactRequestTarget?.let { profile ->
        ContactAccessDialog(
            profile = profile,
            onDismiss = { viewModel.dismissContactRequest() },
            onSubmitRequest = { reason -> viewModel.submitContactRequest(profile.artistIdCode, reason) }
        )
    }
}

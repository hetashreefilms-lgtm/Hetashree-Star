package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.ReelVideo
import com.example.ui.theme.*

@Composable
fun ReelsScreen(
    reels: List<ReelVideo>,
    onBack: () -> Unit,
    onArtistClick: (String) -> Unit,
    onLikeReel: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    onSaveReel: (String) -> Unit
) {
    if (reels.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text("No Reels available", color = Color.White)
        }
        return
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var isMuted by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(true) }
    var showPlayIcon by remember { mutableStateOf(false) }

    val currentReel = reels[currentIndex.coerceIn(0, reels.size - 1)]

    // Immersive Full Screen Container
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(reels.size) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount < -50) {
                        // Swipe Up -> Next Reel
                        if (currentIndex < reels.size - 1) {
                            currentIndex++
                        }
                    } else if (dragAmount > 50) {
                        // Swipe Down -> Previous Reel
                        if (currentIndex > 0) {
                            currentIndex--
                        }
                    }
                }
            }
            .clickable {
                isPlaying = !isPlaying
                showPlayIcon = true
            }
            .testTag("reels_screen_container")
    ) {
        // Video Preview / Simulated Full Screen Player
        AsyncImage(
            model = currentReel.videoThumbnailUrl,
            contentDescription = currentReel.caption,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient shadow over bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.35f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // Top Back & Audio Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("reels_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Feed",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Reels",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            IconButton(
                onClick = { isMuted = !isMuted },
                modifier = Modifier.testTag("reels_mute_toggle")
            ) {
                Icon(
                    imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                    contentDescription = if (isMuted) "Unmute" else "Mute",
                    tint = Color.White
                )
            }
        }

        // Animated Play/Pause center overlay
        LaunchedEffect(showPlayIcon) {
            if (showPlayIcon) {
                kotlinx.coroutines.delay(800)
                showPlayIcon = false
            }
        }

        AnimatedVisibility(
            visible = showPlayIcon,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.PlayArrow else Icons.Default.Pause,
                    contentDescription = "Playback state",
                    tint = HetashreeGoldPrimary,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        // Next/Prev Reel quick buttons on screen edges
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (currentIndex > 0) {
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.5f),
                    modifier = Modifier.clickable { currentIndex-- }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Previous Reel",
                        tint = Color.White,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }

            if (currentIndex < reels.size - 1) {
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.5f),
                    modifier = Modifier.clickable { currentIndex++ }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Next Reel",
                        tint = Color.White,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
        }

        // Right-Side Action Column: Avatar, Like, Comment, Share, Save
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(end = 12.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Artist Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, HetashreeGoldPrimary, CircleShape)
                    .clickable { onArtistClick(currentReel.artistIdCode) }
                    .testTag("reels_artist_avatar")
            ) {
                AsyncImage(
                    model = currentReel.authorAvatarUrl,
                    contentDescription = currentReel.authorName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Like Action
            ReelActionItem(
                icon = if (currentReel.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                tint = if (currentReel.isLiked) HetashreeLikeRed else Color.White,
                count = "${currentReel.likesCount}",
                testTag = "reel_like_btn",
                onClick = { onLikeReel(currentReel.id) }
            )

            // Comment Action
            ReelActionItem(
                icon = Icons.Default.ChatBubbleOutline,
                tint = Color.White,
                count = "${currentReel.commentsCount}",
                testTag = "reel_comment_btn",
                onClick = { onCommentClick(currentReel.id) }
            )

            // Share Action
            ReelActionItem(
                icon = Icons.Default.Share,
                tint = Color.White,
                count = "${currentReel.sharesCount}",
                testTag = "reel_share_btn",
                onClick = { /* share */ }
            )

            // Save Action
            ReelActionItem(
                icon = if (currentReel.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                tint = if (currentReel.isSaved) HetashreeGoldPrimary else Color.White,
                count = if (currentReel.isSaved) "Saved" else "Save",
                testTag = "reel_save_btn",
                onClick = { onSaveReel(currentReel.id) }
            )
        }

        // Bottom Left Info: Username, Artist ID, Caption, Hashtags, Audio
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.8f)
                .navigationBarsPadding()
                .padding(start = 16.dp, bottom = 32.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onArtistClick(currentReel.artistIdCode) }
            ) {
                Text(
                    text = "@${currentReel.authorUsername}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.5.sp
                )
                if (currentReel.isVerified) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified",
                        tint = HetashreeVerifiedBlue,
                        modifier = Modifier.size(14.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = HetashreeGoldPrimary.copy(alpha = 0.25f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeGoldPrimary)
                ) {
                    Text(
                        text = currentReel.artistIdCode,
                        color = HetashreeGoldBright,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = currentReel.caption,
                color = Color.White,
                fontSize = 13.sp,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (currentReel.hashtags.isNotEmpty()) {
                Text(
                    text = currentReel.hashtags.joinToString(" "),
                    color = HetashreeGoldLight,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Audio track ticker
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Audio track",
                    tint = HetashreeGoldPrimary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = currentReel.audioTitle,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 11.5.sp,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun ReelActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    count: String,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = count,
            tint = tint,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = count,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

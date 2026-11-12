package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.*
import com.example.ui.theme.*

enum class ProfileTab {
    POSTS,
    REELS,
    PORTFOLIO_GALLERY,
    PHYSICAL_STATS
}

@Composable
fun ProfileScreen(
    profile: ArtistProfile,
    currentUser: User,
    hasContactAccess: Boolean,
    posts: List<SocialPost>,
    reels: List<ReelVideo>,
    onRequestContactAccess: () -> Unit,
    onMessageClick: () -> Unit,
    onOpenReels: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(ProfileTab.POSTS) }
    var isFaved by remember { mutableStateOf(false) }

    val artistPosts = posts.filter { it.artistIdCode == profile.artistIdCode }
    val artistReels = reels.filter { it.artistIdCode == profile.artistIdCode }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(HetashreeBlack)
            .testTag("artist_profile_screen"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Top Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Row: Avatar + Stats (Posts, Fans, Faves)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Avatar with Gold border
                    Box(
                        modifier = Modifier
                            .size(86.dp)
                            .clip(CircleShape)
                            .border(2.dp, HetashreeGoldPrimary, CircleShape)
                            .padding(2.dp)
                    ) {
                        AsyncImage(
                            model = profile.portfolio.profilePhoto,
                            contentDescription = profile.fullName,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Statistics Row
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileStatItem(count = "${profile.postsCount.coerceAtLeast(artistPosts.size)}", label = "Posts")
                        ProfileStatItem(count = "${profile.fansCount}", label = "FANS")
                        ProfileStatItem(count = "${profile.favesCount}", label = "FAVES")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Full Name & Verified Badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = profile.fullName,
                        color = HetashreeTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.5.sp
                    )
                    if (profile.isVerified) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified Artist",
                            tint = HetashreeVerifiedBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Username & Artist ID
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Text(
                        text = "@${profile.username}",
                        color = HetashreeTextSecondary,
                        fontSize = 12.5.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = HetashreeGoldPrimary.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeGoldPrimary)
                    ) {
                        Text(
                            text = profile.artistIdCode,
                            color = HetashreeGoldBright,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Category badges
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    profile.allCategories.take(3).forEach { cat ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = HetashreeCardElevated
                        ) {
                            Text(
                                text = cat,
                                color = HetashreeGoldLight,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                // Bio
                Text(
                    text = profile.biography,
                    color = HetashreeTextPrimary,
                    fontSize = 12.5.sp,
                    lineHeight = 17.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                // Key Info Pills: Location, Height, Experience
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    InfoPill(icon = Icons.Default.LocationOn, text = profile.currentLocation)
                    InfoPill(icon = Icons.Default.Height, text = "${profile.measurements.heightFeet} (${profile.measurements.heightCm}cm)")
                    InfoPill(icon = Icons.Default.WorkHistory, text = "${profile.actingExperienceYears}y Exp")
                }

                // Private Contact Info Card (Unlocked vs Locked)
                Spacer(modifier = Modifier.height(6.dp))
                if (hasContactAccess) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = HetashreeGoldAmber.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeGoldPrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LockOpen,
                                    contentDescription = "Unlocked Contact",
                                    tint = HetashreeGoldBright,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Verified Direct Contact (Access Granted)",
                                    color = HetashreeGoldBright,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Phone: ${profile.privateMobile}", color = HetashreeTextPrimary, fontSize = 11.5.sp)
                            Text(text = "Email: ${profile.privateEmail}", color = HetashreeTextPrimary, fontSize = 11.5.sp)
                            Text(text = "Address: ${profile.privateAddress}", color = HetashreeTextSecondary, fontSize = 11.sp)
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = HetashreeCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Locked Contact",
                                    tint = HetashreeTextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "Private Contact Information",
                                        color = HetashreeTextPrimary,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Protected under HETASHREE Talent Privacy",
                                        color = HetashreeTextMuted,
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            Button(
                                onClick = onRequestContactAccess,
                                colors = ButtonDefaults.buttonColors(containerColor = HetashreeGoldPrimary),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("request_contact_btn")
                            ) {
                                Text("Request Access", color = HetashreeBlack, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Profile Action Buttons (Fave, Message, Share)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { isFaved = !isFaved },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFaved) HetashreeCardElevated else HetashreeGoldPrimary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("profile_fave_btn")
                    ) {
                        Text(
                            text = if (isFaved) "Faved ★" else "Fave Artist",
                            color = if (isFaved) HetashreeGoldBright else HetashreeBlack,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = onMessageClick,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeBorder),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("profile_message_btn")
                    ) {
                        Text("Message", color = HetashreeTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Tab Navigation
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HetashreeCardElevated)
            ) {
                TabItem(
                    label = "Posts",
                    selected = selectedTab == ProfileTab.POSTS,
                    icon = Icons.Default.GridOn,
                    modifier = Modifier.weight(1f)
                ) { selectedTab = ProfileTab.POSTS }

                TabItem(
                    label = "Reels",
                    selected = selectedTab == ProfileTab.REELS,
                    icon = Icons.Default.VideoLibrary,
                    modifier = Modifier.weight(1f)
                ) { selectedTab = ProfileTab.REELS }

                TabItem(
                    label = "Portfolio",
                    selected = selectedTab == ProfileTab.PORTFOLIO_GALLERY,
                    icon = Icons.Default.Collections,
                    modifier = Modifier.weight(1f)
                ) { selectedTab = ProfileTab.PORTFOLIO_GALLERY }

                TabItem(
                    label = "Stats",
                    selected = selectedTab == ProfileTab.PHYSICAL_STATS,
                    icon = Icons.Default.PersonSearch,
                    modifier = Modifier.weight(1f)
                ) { selectedTab = ProfileTab.PHYSICAL_STATS }
            }
        }

        // Tab Content
        when (selectedTab) {
            ProfileTab.POSTS -> {
                if (artistPosts.isEmpty()) {
                    item {
                        EmptyTabNotice("No posts uploaded yet.")
                    }
                } else {
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            val chunks = artistPosts.chunked(3)
                            for (row in chunks) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    for (p in row) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .aspectRatio(1f)
                                                .background(HetashreeCard)
                                        ) {
                                            AsyncImage(
                                                model = p.mediaUrl,
                                                contentDescription = p.caption,
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                    }
                                    repeat(3 - row.size) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                            }
                        }
                    }
                }
            }

            ProfileTab.REELS -> {
                if (artistReels.isEmpty()) {
                    item {
                        EmptyTabNotice("No Reels available.")
                    }
                } else {
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            val chunks = artistReels.chunked(3)
                            for (row in chunks) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    for (r in row) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .aspectRatio(0.6f)
                                                .background(HetashreeCard)
                                                .clickable { onOpenReels() }
                                        ) {
                                            AsyncImage(
                                                model = r.videoThumbnailUrl,
                                                contentDescription = r.caption,
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.BottomStart)
                                                    .padding(4.dp)
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                                    Text("${r.likesCount}", color = Color.White, fontSize = 9.sp)
                                                }
                                            }
                                        }
                                    }
                                    repeat(3 - row.size) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                            }
                        }
                    }
                }
            }

            ProfileTab.PORTFOLIO_GALLERY -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Professional Industry Portfolio",
                            color = HetashreeGoldBright,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        PortfolioCategorySection(
                            title = "Required Angles & Looks",
                            photos = listOf(
                                "Close-up" to profile.portfolio.closeUpPhoto,
                                "Half-length" to profile.portfolio.halfPhoto,
                                "Full-length" to profile.portfolio.fullLengthPhoto,
                                "Traditional" to profile.portfolio.traditionalPhoto,
                                "Western" to profile.portfolio.westernPhoto,
                                "Professional" to profile.portfolio.professionalPhoto
                            )
                        )

                        Text(
                            text = "Audition & Introduction Videos",
                            color = HetashreeGoldBright,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = HetashreeCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeBorder),
                            modifier = Modifier.fillMaxWidth().clickable { onOpenReels() }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.MovieCreation, contentDescription = "Audition Tape", tint = HetashreeGoldPrimary)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Official FTII Monologue Audition Tape", color = HetashreeTextPrimary, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                                    Text("Format: 1080p Full HD • Verified by HETASHREE Casting", color = HetashreeTextSecondary, fontSize = 10.5.sp)
                                }
                            }
                        }
                    }
                }
            }

            ProfileTab.PHYSICAL_STATS -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Detailed Physical Measurements", color = HetashreeGoldBright, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = HetashreeCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                MeasurementRow("Height", "${profile.measurements.heightFeet} / ${profile.measurements.heightCm} cm")
                                MeasurementRow("Weight", "${profile.measurements.weightKg} kg")
                                MeasurementRow("Chest / Bust", "${profile.measurements.chestInches} inches")
                                MeasurementRow("Waist", "${profile.measurements.waistInches} inches")
                                MeasurementRow("Hips", "${profile.measurements.hipsInches} inches")
                                MeasurementRow("Shoe Size", profile.measurements.shoeSize)
                                MeasurementRow("Clothing Size", profile.measurements.clothingSize)
                                MeasurementRow("Hair Color", profile.measurements.hairColor)
                                MeasurementRow("Eye Color", profile.measurements.eyeColor)
                                MeasurementRow("Native Place", profile.nativePlace)
                                MeasurementRow("Languages", profile.languages.joinToString(", "))
                                MeasurementRow("Key Skills", profile.skills.joinToString(", "))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileStatItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            color = HetashreeTextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = label,
            color = if (label == "FANS" || label == "FAVES") HetashreeGoldPrimary else HetashreeTextSecondary,
            fontSize = 11.5.sp,
            fontWeight = if (label == "FANS" || label == "FAVES") FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun InfoPill(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = HetashreeCardElevated
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = HetashreeGoldLight, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = text, color = HetashreeTextSecondary, fontSize = 10.5.sp)
        }
    }
}

@Composable
private fun TabItem(
    label: String,
    selected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) HetashreeGoldPrimary else HetashreeTextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                color = if (selected) HetashreeGoldPrimary else HetashreeTextSecondary,
                fontSize = 10.5.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun EmptyTabNotice(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message, color = HetashreeTextSecondary, fontSize = 13.sp)
    }
}

@Composable
private fun PortfolioCategorySection(
    title: String,
    photos: List<Pair<String, String>>
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = title, color = HetashreeTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            photos.take(3).forEach { (label, url) ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(0.85f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(HetashreeCard)
                ) {
                    AsyncImage(
                        model = url,
                        contentDescription = label,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = label,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .background(Color.Black.copy(alpha = 0.6f))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MeasurementRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = HetashreeTextSecondary, fontSize = 12.sp)
        Text(text = value, color = HetashreeTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

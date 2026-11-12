package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
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
import com.example.model.ArtistProfile
import com.example.model.ReelVideo
import com.example.model.SocialPost
import com.example.ui.theme.*
import com.example.viewmodel.AppDestination

@Composable
fun DiscoverScreen(
    profiles: List<ArtistProfile>,
    posts: List<SocialPost>,
    reels: List<ReelVideo>,
    onArtistClick: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onOpenReels: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf(
        "All",
        "Lead Artists",
        "Female Models",
        "Male Models",
        "Kids Artists",
        "Character Artists",
        "Influencers",
        "Dancers",
        "Singers"
    )

    val filteredProfiles = remember(searchQuery, selectedCategory, profiles) {
        profiles.filter { profile ->
            val matchesQuery = searchQuery.isBlank() ||
                    profile.fullName.contains(searchQuery, ignoreCase = true) ||
                    profile.artistIdCode.contains(searchQuery, ignoreCase = true) ||
                    profile.currentLocation.contains(searchQuery, ignoreCase = true) ||
                    profile.skills.any { it.contains(searchQuery, ignoreCase = true) }

            val matchesCategory = selectedCategory == "All" ||
                    profile.allCategories.contains(selectedCategory) ||
                    profile.primaryCategory == selectedCategory

            matchesQuery && matchesCategory
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(HetashreeBlack)
            .testTag("discover_screen_list"),
        contentPadding = PaddingValues(bottom = 72.dp)
    ) {
        // Search & Filter Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search by name, ID (e.g. HT-ART-000001), city or skill", color = HetashreeTextMuted, fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = HetashreeGoldPrimary)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = HetashreeTextPrimary,
                            unfocusedTextColor = HetashreeTextPrimary,
                            focusedBorderColor = HetashreeGoldPrimary,
                            unfocusedBorderColor = HetashreeBorder,
                            focusedContainerColor = HetashreeCard,
                            unfocusedContainerColor = HetashreeCard
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("discover_search_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        color = HetashreeCardElevated,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .clickable { onNavigateToSearch() }
                            .testTag("advanced_filter_btn")
                    ) {
                        Box(
                            modifier = Modifier.padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Advanced Talent Filter",
                                tint = HetashreeGoldPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Category Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { cat ->
                        val isSelected = selectedCategory == cat
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) HetashreeGoldPrimary else HetashreeCard,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) HetashreeGoldBright else HetashreeBorder
                            ),
                            modifier = Modifier.clickable { selectedCategory = cat }
                        ) {
                            Text(
                                text = cat,
                                color = if (isSelected) HetashreeBlack else HetashreeTextPrimary,
                                fontSize = 11.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Recommended Talent Showcase (Horizontal)
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Featured Industry Talent",
                        color = HetashreeGoldBright,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "View All",
                        color = HetashreeGoldLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { onNavigateToSearch() }
                    )
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(filteredProfiles) { profile ->
                        TalentSpotlightCard(
                            profile = profile,
                            onClick = { onArtistClick(profile.artistIdCode) }
                        )
                    }
                }
            }
        }

        // Trending Media Grid (Posts & Reels)
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp)) {
                Text(
                    text = "Explore Portfolios & Reels",
                    color = HetashreeGoldBright,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Staggered / 3-column media preview
                val allMedia = remember(posts, reels) {
                    val mediaList = mutableListOf<MediaExploreItem>()
                    posts.forEach {
                        mediaList.add(MediaExploreItem(it.id, it.mediaUrl, it.artistIdCode, isReel = false, caption = it.caption))
                    }
                    reels.forEach {
                        mediaList.add(MediaExploreItem(it.id, it.videoThumbnailUrl, it.artistIdCode, isReel = true, caption = it.caption))
                    }
                    mediaList
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    val chunks = allMedia.chunked(3)
                    for (row in chunks) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            for (item in row) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(HetashreeCard)
                                        .clickable {
                                            if (item.isReel) {
                                                onOpenReels()
                                            } else {
                                                onArtistClick(item.artistIdCode)
                                            }
                                        }
                                        .testTag("explore_media_${item.id}")
                                ) {
                                    AsyncImage(
                                        model = item.mediaUrl,
                                        contentDescription = item.caption,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )

                                    if (item.isReel) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(4.dp)
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(Color.Black.copy(alpha = 0.6f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.PlayArrow,
                                                contentDescription = "Reel",
                                                tint = Color.White,
                                                modifier = Modifier.size(12.dp)
                                            )
                                        }
                                    }

                                    // Artist ID code on bottom
                                    Text(
                                        text = item.artistIdCode,
                                        color = Color.White,
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                            .background(Color.Black.copy(alpha = 0.7f))
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            // Filler if less than 3
                            repeat(3 - row.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class MediaExploreItem(
    val id: String,
    val mediaUrl: String,
    val artistIdCode: String,
    val isReel: Boolean,
    val caption: String
)

@Composable
private fun TalentSpotlightCard(
    profile: ArtistProfile,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = HetashreeCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeBorder),
        modifier = Modifier
            .width(140.dp)
            .clickable { onClick() }
            .testTag("talent_card_${profile.artistIdCode}")
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, HetashreeGoldPrimary, CircleShape)
            ) {
                AsyncImage(
                    model = profile.portfolio.profilePhoto,
                    contentDescription = profile.fullName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = profile.fullName,
                color = HetashreeTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 12.5.sp,
                maxLines = 1
            )

            Text(
                text = profile.artistIdCode,
                color = HetashreeGoldPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 10.sp
            )

            Text(
                text = "${profile.primaryCategory} • ${profile.age}y",
                color = HetashreeTextSecondary,
                fontSize = 10.sp,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = HetashreeGoldPrimary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "View Portfolio",
                    color = HetashreeBlack,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

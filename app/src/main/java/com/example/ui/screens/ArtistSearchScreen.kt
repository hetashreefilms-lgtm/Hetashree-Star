package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.ArtistProfile
import com.example.ui.theme.*

@Composable
fun ArtistSearchScreen(
    profiles: List<ArtistProfile>,
    onBack: () -> Unit,
    onArtistSelect: (String) -> Unit,
    onRequestContactAccess: (ArtistProfile) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All Categories") }
    var selectedGender by remember { mutableStateOf("All") }
    var selectedLocation by remember { mutableStateOf("All Cities") }
    var selectedAgeGroup by remember { mutableStateOf("All Ages") }

    val categories = listOf(
        "All Categories",
        "Lead Artists",
        "Supporting Artists",
        "Character Artists",
        "Senior Artists",
        "Background Artists",
        "Female Models",
        "Male Models",
        "Kids Artists",
        "Influencers",
        "Dancers",
        "Singers",
        "Voice Artists"
    )

    val locations = listOf(
        "All Cities",
        "Mumbai",
        "Pune",
        "Hyderabad",
        "Delhi",
        "Bangalore",
        "Jaipur",
        "Kolkata"
    )

    val ageGroups = listOf(
        "All Ages",
        "0–4",
        "5–8",
        "9–12",
        "13–17",
        "18–25",
        "26–35",
        "36–45",
        "46–59",
        "60+"
    )

    val filteredProfiles = remember(
        profiles,
        searchQuery,
        selectedCategory,
        selectedGender,
        selectedLocation,
        selectedAgeGroup
    ) {
        profiles.filter { profile ->
            val matchesQuery = searchQuery.isBlank() ||
                    profile.fullName.contains(searchQuery, ignoreCase = true) ||
                    profile.artistIdCode.contains(searchQuery, ignoreCase = true) ||
                    profile.skills.any { it.contains(searchQuery, ignoreCase = true) } ||
                    profile.languages.any { it.contains(searchQuery, ignoreCase = true) }

            val matchesCategory = selectedCategory == "All Categories" ||
                    profile.allCategories.contains(selectedCategory) ||
                    profile.primaryCategory == selectedCategory

            val matchesGender = selectedGender == "All" ||
                    profile.gender.equals(selectedGender, ignoreCase = true)

            val matchesLocation = selectedLocation == "All Cities" ||
                    profile.currentLocation.contains(selectedLocation, ignoreCase = true)

            val matchesAge = when (selectedAgeGroup) {
                "0–4" -> profile.age in 0..4
                "5–8" -> profile.age in 5..8
                "9–12" -> profile.age in 9..12
                "13–17" -> profile.age in 13..17
                "18–25" -> profile.age in 18..25
                "26–35" -> profile.age in 26..35
                "36–45" -> profile.age in 36..45
                "46–59" -> profile.age in 46..59
                "60+" -> profile.age >= 60
                else -> true
            }

            matchesQuery && matchesCategory && matchesGender && matchesLocation && matchesAge
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(HetashreeBlack)
            .testTag("artist_search_screen"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Top Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HetashreeDarkSurface)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("search_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = HetashreeTextPrimary)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Professional Artist Discovery",
                            color = HetashreeGoldBright,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Advanced Casting Filter & Talent Engine",
                            color = HetashreeTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Search field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search name, ID (HT-ART-...), dialect, martial arts, skills...", color = HetashreeTextMuted, fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = HetashreeGoldPrimary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = HetashreeTextPrimary,
                        unfocusedTextColor = HetashreeTextPrimary,
                        focusedBorderColor = HetashreeGoldPrimary,
                        unfocusedBorderColor = HetashreeBorder,
                        focusedContainerColor = HetashreeCard,
                        unfocusedContainerColor = HetashreeCard
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().testTag("advanced_search_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Filter row 1: Category Chips
                Text("Category:", color = HetashreeTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        FilterPill(
                            label = cat,
                            isSelected = selectedCategory == cat,
                            onClick = { selectedCategory = cat }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Filter row 2: Age Groups
                Text("Age Group:", color = HetashreeTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(ageGroups) { age ->
                        FilterPill(
                            label = age,
                            isSelected = selectedAgeGroup == age,
                            onClick = { selectedAgeGroup = age }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Filter row 3: Multi-City Location
                Text("Location:", color = HetashreeTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(locations) { loc ->
                        FilterPill(
                            label = loc,
                            isSelected = selectedLocation == loc,
                            onClick = { selectedLocation = loc }
                        )
                    }
                }
            }
            HorizontalDivider(color = HetashreeBorder)
        }

        // Results Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Matching Talent (${filteredProfiles.size})",
                    color = HetashreeTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "Secure Contact Shield Active",
                    color = HetashreeGoldAmber,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Talent Cards
        if (filteredProfiles.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No artists match your specific filters. Try expanding criteria.", color = HetashreeTextSecondary, fontSize = 13.sp)
                }
            }
        } else {
            items(filteredProfiles, key = { it.artistIdCode }) { profile ->
                ArtistSearchResultCard(
                    profile = profile,
                    onSelect = { onArtistSelect(profile.artistIdCode) },
                    onRequestContact = { onRequestContactAccess(profile) }
                )
            }
        }
    }
}

@Composable
private fun FilterPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) HetashreeGoldPrimary else HetashreeCard,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) HetashreeGoldBright else HetashreeBorder
        ),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            color = if (isSelected) HetashreeBlack else HetashreeTextPrimary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun ArtistSearchResultCard(
    profile: ArtistProfile,
    onSelect: () -> Unit,
    onRequestContact: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = HetashreeCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 5.dp)
            .clickable { onSelect() }
            .testTag("artist_result_${profile.artistIdCode}")
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile photo
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, HetashreeGoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = profile.portfolio.profilePhoto,
                    contentDescription = profile.fullName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = profile.fullName,
                        color = HetashreeTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    if (profile.isVerified) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = HetashreeVerifiedBlue,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = profile.artistIdCode,
                        color = HetashreeGoldPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• ${profile.age} yrs • ${profile.measurements.heightFeet}",
                        color = HetashreeTextSecondary,
                        fontSize = 11.sp
                    )
                }

                Text(
                    text = "${profile.primaryCategory} • ${profile.currentLocation}",
                    color = HetashreeGoldLight,
                    fontSize = 11.sp
                )

                Text(
                    text = "Skills: ${profile.skills.take(3).joinToString(", ")}",
                    color = HetashreeTextMuted,
                    fontSize = 10.5.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = HetashreeGoldPrimary,
                        modifier = Modifier.clickable { onSelect() }
                    ) {
                        Text(
                            text = "View Portfolio",
                            color = HetashreeBlack,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = HetashreeCardElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeBorder),
                        modifier = Modifier.clickable { onRequestContact() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = HetashreeGoldAmber, modifier = Modifier.size(11.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("Request Contact", color = HetashreeTextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}

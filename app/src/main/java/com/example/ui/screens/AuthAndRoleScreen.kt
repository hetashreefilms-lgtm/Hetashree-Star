package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.MockInitialData
import com.example.model.User
import com.example.model.UserRole
import com.example.ui.theme.*
import java.util.Calendar

@Composable
fun AuthAndRoleScreen(
    currentUser: User,
    onBack: () -> Unit,
    onSwitchUser: (User) -> Unit,
    onRegisterArtist: (
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
    ) -> Unit
) {
    var showRegisterForm by remember { mutableStateOf(false) }

    // Registration state
    var fullName by remember { mutableStateOf("") }
    var birthYear by remember { mutableStateOf("2000") }
    var birthMonth by remember { mutableStateOf("06") }
    var birthDay by remember { mutableStateOf("15") }
    var gender by remember { mutableStateOf("Female") }
    var primaryCategory by remember { mutableStateOf("Lead Artists") }
    var currentLocation by remember { mutableStateOf("Mumbai, Maharashtra") }
    var nativePlace by remember { mutableStateOf("Delhi") }
    var stateBelonging by remember { mutableStateOf("Maharashtra") }
    var languagesText by remember { mutableStateOf("Hindi, English") }
    var experienceText by remember { mutableStateOf("3") }
    var skillsText by remember { mutableStateOf("Classical Dance, Screen Acting") }
    var bio by remember { mutableStateOf("Passionate actor trained in theater and screen arts.") }
    var mobile by remember { mutableStateOf("+91 98200 88990") }
    var email by remember { mutableStateOf("artist.new@hetashreestar.com") }
    var heightCmText by remember { mutableStateOf("170") }
    var photoUrl by remember { mutableStateOf("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500") }

    val calculatedAge = remember(birthYear) {
        val currentYear = 2026
        val year = birthYear.toIntOrNull() ?: 2000
        (currentYear - year).coerceAtLeast(1)
    }

    val sampleUsers = listOf(
        MockInitialData.adminUser,
        MockInitialData.priyaUser,
        MockInitialData.aryanUser,
        MockInitialData.ananyaUser,
        MockInitialData.producerUser,
        MockInitialData.directorUser,
        MockInitialData.castingDirectorUser
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(HetashreeBlack)
            .testTag("auth_and_role_screen"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HetashreeDarkSurface)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("auth_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = HetashreeTextPrimary)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Account & Role Management",
                            color = HetashreeGoldBright,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "HETASHREE FILM'S PRODUCTION • Multi-Role Access",
                            color = HetashreeTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
            HorizontalDivider(color = HetashreeBorder)
        }

        // Toggle Between Quick Switch & Register New Artist
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showRegisterForm = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!showRegisterForm) HetashreeGoldPrimary else HetashreeCard
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Switch Account",
                        color = if (!showRegisterForm) HetashreeBlack else HetashreeTextPrimary,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = { showRegisterForm = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (showRegisterForm) HetashreeGoldPrimary else HetashreeCard
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).testTag("tab_register_artist")
                ) {
                    Text(
                        text = "Register New Artist",
                        color = if (showRegisterForm) HetashreeBlack else HetashreeTextPrimary,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (!showRegisterForm) {
            // Quick Role Switcher
            item {
                Text(
                    text = "Select Persona to Test All Roles & Permissions:",
                    color = HetashreeTextSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                )
            }

            items(sampleUsers, key = { it.id }) { user ->
                val isActive = currentUser.id == user.id
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isActive) HetashreeCardElevated else HetashreeCard,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isActive) HetashreeGoldPrimary else HetashreeBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 5.dp)
                        .clickable { onSwitchUser(user) }
                        .testTag("switch_user_${user.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, if (isActive) HetashreeGoldPrimary else HetashreeBorder, CircleShape)
                        ) {
                            AsyncImage(
                                model = user.avatarUrl,
                                contentDescription = user.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = user.name,
                                    color = HetashreeTextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                if (user.isVerified) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Verified",
                                        tint = HetashreeVerifiedBlue,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            }

                            Text(
                                text = "Role: ${user.role.displayName} ${user.artistIdCode?.let { "• $it" } ?: ""}",
                                color = HetashreeGoldBright,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = user.companyName ?: user.email,
                                color = HetashreeTextMuted,
                                fontSize = 10.5.sp
                            )
                        }

                        if (isActive) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = HetashreeGoldPrimary
                            ) {
                                Text(
                                    text = "ACTIVE",
                                    color = HetashreeBlack,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // New Artist Registration Form (Part 4 & 5)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = HetashreeGoldAmber.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeGoldAmber)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Automatic Unique Sequential Artist ID",
                                color = HetashreeGoldBright,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "New artist will be assigned the next sequential ID (e.g. HT-ART-000004). Never recycled or duplicated.",
                                color = HetashreeTextSecondary,
                                fontSize = 10.5.sp
                            )
                        }
                    }

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Legal Name") },
                        modifier = Modifier.fillMaxWidth().testTag("reg_name_input")
                    )

                    // Date of Birth & Auto Calculated Age
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = birthYear,
                            onValueChange = { birthYear = it },
                            label = { Text("Year (YYYY)") },
                            modifier = Modifier.weight(1.2f).testTag("reg_year_input")
                        )
                        OutlinedTextField(
                            value = birthMonth,
                            onValueChange = { birthMonth = it },
                            label = { Text("Month (MM)") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = birthDay,
                            onValueChange = { birthDay = it },
                            label = { Text("Day (DD)") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = HetashreeCardElevated,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Auto-Calculated Age: $calculatedAge years (${if (calculatedAge < 18) "Kids Artist with Parent Consent" else "Adult Artist"})",
                            color = HetashreeGoldLight,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = gender,
                            onValueChange = { gender = it },
                            label = { Text("Gender (Male/Female/Kid)") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = primaryCategory,
                            onValueChange = { primaryCategory = it },
                            label = { Text("Primary Category") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = currentLocation,
                            onValueChange = { currentLocation = it },
                            label = { Text("Current City") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = heightCmText,
                            onValueChange = { heightCmText = it },
                            label = { Text("Height (cm)") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = mobile,
                        onValueChange = { mobile = it },
                        label = { Text("Mobile Number (Private/Guarded)") },
                        modifier = Modifier.fillMaxWidth().testTag("reg_mobile_input")
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address (Private/Guarded)") },
                        modifier = Modifier.fillMaxWidth().testTag("reg_email_input")
                    )

                    OutlinedTextField(
                        value = languagesText,
                        onValueChange = { languagesText = it },
                        label = { Text("Languages Spoken (comma separated)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = skillsText,
                        onValueChange = { skillsText = it },
                        label = { Text("Skills & Special Talents") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = bio,
                        onValueChange = { bio = it },
                        label = { Text("Professional Biography") },
                        modifier = Modifier.fillMaxWidth().height(90.dp)
                    )

                    Button(
                        onClick = {
                            if (fullName.isNotBlank()) {
                                val dob = "$birthYear-$birthMonth-$birthDay"
                                val langs = languagesText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                                val sks = skillsText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                                val exp = experienceText.toIntOrNull() ?: 0
                                val ht = heightCmText.toIntOrNull() ?: 170

                                onRegisterArtist(
                                    fullName, dob, calculatedAge, gender, primaryCategory,
                                    currentLocation, nativePlace, stateBelonging, langs, exp,
                                    sks, bio, mobile, email, ht, photoUrl
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = HetashreeGoldPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("submit_registration_btn")
                    ) {
                        Text(
                            text = "Register Artist & Generate Unique Artist ID",
                            color = HetashreeBlack,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

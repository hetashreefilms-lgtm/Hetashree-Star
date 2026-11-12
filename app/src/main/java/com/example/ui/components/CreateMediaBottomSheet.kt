package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CastingCall
import com.example.model.User
import com.example.model.UserRole
import com.example.ui.theme.*

enum class CreateMode {
    MENU,
    NEW_POST,
    NEW_REEL,
    NEW_STORY,
    NEW_CASTING_CALL
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMediaBottomSheet(
    currentUser: User,
    onDismiss: () -> Unit,
    onCreatePost: (mediaUrl: String, caption: String, hashtags: List<String>) -> Unit,
    onCreateReel: (thumbUrl: String, caption: String, audio: String, hashtags: List<String>) -> Unit,
    onAddStory: (mediaUrl: String, caption: String) -> Unit,
    onCreateCastingCall: (CastingCall) -> Unit
) {
    var mode by remember { mutableStateOf(CreateMode.MENU) }

    // Form states
    var mediaUrl by remember { mutableStateOf("") }
    var caption by remember { mutableStateOf("") }
    var hashtagsText by remember { mutableStateOf("#HetashreeStar #DreamCastCreate") }
    var audioTrack by remember { mutableStateOf("HETASHREE Studio Beats") }

    // Casting Call form states
    var projectName by remember { mutableStateOf("") }
    var roleName by remember { mutableStateOf("") }
    var projectType by remember { mutableStateOf("Feature Films") }
    var category by remember { mutableStateOf("Lead Artists") }
    var gender by remember { mutableStateOf("Female") }
    var location by remember { mutableStateOf("Mumbai") }
    var budget by remember { mutableStateOf("250000") }
    var workingDays by remember { mutableStateOf("15") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = HetashreeDarkSurface,
        tonalElevation = 8.dp,
        modifier = Modifier.testTag("create_media_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            when (mode) {
                CreateMode.MENU -> {
                    Text(
                        text = "Create with HETASHREE STAR",
                        color = HetashreeGoldBright,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Share entertainment updates, auditions or post casting calls",
                        color = HetashreeTextSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    CreateOptionRow(
                        icon = Icons.Default.AddPhotoAlternate,
                        title = "Create Post / Upload Photo",
                        subtitle = "Share professional portfolio portraits or shoot updates",
                        testTag = "create_option_post"
                    ) {
                        mediaUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=900"
                        mode = CreateMode.NEW_POST
                    }

                    CreateOptionRow(
                        icon = Icons.Default.VideoCameraBack,
                        title = "Create Reel / Audition Video",
                        subtitle = "Vertical video showcase, monologues & dance clips",
                        testTag = "create_option_reel"
                    ) {
                        mediaUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=900"
                        mode = CreateMode.NEW_REEL
                    }

                    CreateOptionRow(
                        icon = Icons.Default.HistoryToggleOff,
                        title = "Add to Story",
                        subtitle = "Behind-the-scenes ephemeral 24h photo or video update",
                        testTag = "create_option_story"
                    ) {
                        mediaUrl = "https://images.unsplash.com/photo-1509631179647-0177331693ae?w=800"
                        mode = CreateMode.NEW_STORY
                    }

                    if (currentUser.role in listOf(UserRole.PRODUCER, UserRole.DIRECTOR, UserRole.CASTING_DIRECTOR, UserRole.SUPER_ADMIN, UserRole.AUTHORIZED_ADMIN)) {
                        CreateOptionRow(
                            icon = Icons.Default.Campaign,
                            title = "Post Casting Call Requirement",
                            subtitle = "Publish roles for Films, Serials, OTT, Ads or Shows",
                            testTag = "create_option_casting"
                        ) {
                            mode = CreateMode.NEW_CASTING_CALL
                        }
                    }
                }

                CreateMode.NEW_POST -> {
                    Text(
                        text = "Publish Social Post",
                        color = HetashreeGoldBright,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = mediaUrl,
                        onValueChange = { mediaUrl = it },
                        label = { Text("Photo URL / Portfolio Asset") },
                        modifier = Modifier.fillMaxWidth().testTag("post_url_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = caption,
                        onValueChange = { caption = it },
                        label = { Text("Caption (e.g. Look test for new project)") },
                        modifier = Modifier.fillMaxWidth().height(100.dp).testTag("post_caption_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = hashtagsText,
                        onValueChange = { hashtagsText = it },
                        label = { Text("Hashtags") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (caption.isNotBlank() || mediaUrl.isNotBlank()) {
                                val tags = hashtagsText.split(" ").filter { it.isNotBlank() }
                                onCreatePost(mediaUrl, caption.ifBlank { "New portfolio capture! #HetashreeStar" }, tags)
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = HetashreeGoldPrimary),
                        modifier = Modifier.fillMaxWidth().testTag("publish_post_button")
                    ) {
                        Text("Share Post", color = HetashreeBlack, fontWeight = FontWeight.Bold)
                    }
                }

                CreateMode.NEW_REEL -> {
                    Text(
                        text = "Publish Vertical Reel",
                        color = HetashreeGoldBright,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = mediaUrl,
                        onValueChange = { mediaUrl = it },
                        label = { Text("Video Cover Thumbnail URL") },
                        modifier = Modifier.fillMaxWidth().testTag("reel_thumb_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = caption,
                        onValueChange = { caption = it },
                        label = { Text("Reel Caption") },
                        modifier = Modifier.fillMaxWidth().testTag("reel_caption_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = audioTrack,
                        onValueChange = { audioTrack = it },
                        label = { Text("Audio Track Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val tags = hashtagsText.split(" ").filter { it.isNotBlank() }
                            onCreateReel(mediaUrl, caption.ifBlank { "Audition Reel #DreamCastCreate" }, audioTrack, tags)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = HetashreeGoldPrimary),
                        modifier = Modifier.fillMaxWidth().testTag("publish_reel_button")
                    ) {
                        Text("Publish Reel", color = HetashreeBlack, fontWeight = FontWeight.Bold)
                    }
                }

                CreateMode.NEW_STORY -> {
                    Text(
                        text = "Add to 24h Story",
                        color = HetashreeGoldBright,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = mediaUrl,
                        onValueChange = { mediaUrl = it },
                        label = { Text("Story Photo / Video URL") },
                        modifier = Modifier.fillMaxWidth().testTag("story_url_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = caption,
                        onValueChange = { caption = it },
                        label = { Text("Short Story Tag (Optional)") },
                        modifier = Modifier.fillMaxWidth().testTag("story_caption_input")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onAddStory(mediaUrl, caption)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = HetashreeGoldPrimary),
                        modifier = Modifier.fillMaxWidth().testTag("publish_story_button")
                    ) {
                        Text("Add to Your Story", color = HetashreeBlack, fontWeight = FontWeight.Bold)
                    }
                }

                CreateMode.NEW_CASTING_CALL -> {
                    Text(
                        text = "Create Official Casting Call",
                        color = HetashreeGoldBright,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = projectName,
                        onValueChange = { projectName = it },
                        label = { Text("Project Title") },
                        modifier = Modifier.fillMaxWidth().testTag("casting_title_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = roleName,
                        onValueChange = { roleName = it },
                        label = { Text("Role Name & Description") },
                        modifier = Modifier.fillMaxWidth().testTag("casting_role_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = { category = it },
                            label = { Text("Category") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = gender,
                            onValueChange = { gender = it },
                            label = { Text("Gender") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = location,
                            onValueChange = { location = it },
                            label = { Text("Location") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = budget,
                            onValueChange = { budget = it },
                            label = { Text("Budget (₹)") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (projectName.isNotBlank() && roleName.isNotBlank()) {
                                val call = CastingCall(
                                    id = "CAST-2026-${System.currentTimeMillis() % 1000}",
                                    projectName = projectName,
                                    projectType = projectType,
                                    productionHouse = currentUser.companyName ?: "HETASHREE FILM'S PRODUCTION",
                                    roleName = roleName,
                                    artistCategory = category,
                                    gender = gender,
                                    minAge = 18,
                                    maxAge = 35,
                                    minHeightCm = 160,
                                    maxHeightCm = 185,
                                    location = location,
                                    shootingDates = "To be scheduled",
                                    workingDays = workingDays.toIntOrNull() ?: 10,
                                    languages = listOf("Hindi", "English"),
                                    experienceRequired = "Portfolio verification required",
                                    requiredSkills = listOf("Screen Acting", "Expressions"),
                                    projectDescription = "Professional entertainment project managed under HETASHREE CASTING & TALENT standards.",
                                    budgetAmount = budget.toDoubleOrNull() ?: 100000.0,
                                    auditionDate = "Upcoming",
                                    auditionLocation = "Studio / Online Tape",
                                    applicationDeadline = "10 days",
                                    status = "Active",
                                    postedBy = currentUser.name
                                )
                                onCreateCastingCall(call)
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = HetashreeGoldPrimary),
                        modifier = Modifier.fillMaxWidth().testTag("publish_casting_button")
                    ) {
                        Text("Publish Casting Call", color = HetashreeBlack, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateOptionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        color = HetashreeCard,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(HetashreeCardElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = HetashreeGoldPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    color = HetashreeTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    color = HetashreeTextSecondary,
                    fontSize = 11.sp
                )
            }
        }
    }
}

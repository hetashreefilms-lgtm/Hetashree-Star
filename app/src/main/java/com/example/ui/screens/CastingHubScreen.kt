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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*
import com.example.viewmodel.AppDestination
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CastingHubScreen(
    currentUser: User,
    castingCalls: List<CastingCall>,
    applications: List<CastingApplication>,
    onApplyToCall: (String) -> Unit,
    onNavigate: (AppDestination) -> Unit,
    onUpdateApplicationStatus: (String, ApplicationStatus) -> Unit
) {
    var selectedProjectType by remember { mutableStateOf("All") }
    var showMyApplicationsOnly by remember { mutableStateOf(false) }

    val projectTypes = listOf(
        "All",
        "Feature Films",
        "Web Series",
        "Commercials",
        "Jewellery Shoots",
        "Fashion Shows",
        "Music Videos"
    )

    val userArtistId = currentUser.artistIdCode ?: "HT-ART-000001"
    val userApplications = applications.filter { it.artistIdCode == userArtistId }

    val isProfessionalOrAdmin = currentUser.role in listOf(
        UserRole.PRODUCER,
        UserRole.DIRECTOR,
        UserRole.CASTING_DIRECTOR,
        UserRole.SUPER_ADMIN,
        UserRole.AUTHORIZED_ADMIN
    )

    val filteredCalls = castingCalls.filter { call ->
        selectedProjectType == "All" || call.projectType.contains(selectedProjectType, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(HetashreeBlack)
            .testTag("casting_hub_screen"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Quick Navigation Bar for Professional Sub-modules
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HetashreeDarkSurface)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "HETASHREE CASTING & TALENT MANAGEMENT",
                    color = HetashreeGoldBright,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = "Official Casting Calls, Verified Agreements & Production Payouts",
                    color = HetashreeTextSecondary,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Navigation shortcuts
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CastingModuleShortcut(
                        icon = Icons.Default.Search,
                        label = "Talent Search",
                        modifier = Modifier.weight(1f)
                    ) { onNavigate(AppDestination.ARTIST_SEARCH) }

                    CastingModuleShortcut(
                        icon = Icons.Default.Description,
                        label = "Agreements",
                        modifier = Modifier.weight(1f)
                    ) { onNavigate(AppDestination.AGREEMENTS) }

                    CastingModuleShortcut(
                        icon = Icons.Default.Payment,
                        label = "Payments",
                        modifier = Modifier.weight(1f)
                    ) { onNavigate(AppDestination.PAYMENTS) }

                    CastingModuleShortcut(
                        icon = Icons.Default.CalendarMonth,
                        label = "Calendar",
                        modifier = Modifier.weight(1f)
                    ) { onNavigate(AppDestination.CALENDAR) }
                }
            }
            HorizontalDivider(color = HetashreeBorder)
        }

        // Filter / My Applications Switch
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (showMyApplicationsOnly) "My Applications (${userApplications.size})" else "Active Casting Calls (${filteredCalls.size})",
                        color = HetashreeTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    FilterChip(
                        selected = showMyApplicationsOnly,
                        onClick = { showMyApplicationsOnly = !showMyApplicationsOnly },
                        label = {
                            Text(
                                text = if (showMyApplicationsOnly) "Show All Calls" else "My Applications",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = HetashreeGoldPrimary,
                            selectedLabelColor = HetashreeBlack,
                            containerColor = HetashreeCardElevated,
                            labelColor = HetashreeTextPrimary
                        )
                    )
                }

                if (!showMyApplicationsOnly) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(projectTypes) { type ->
                            val isSelected = selectedProjectType == type
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) HetashreeGoldPrimary else HetashreeCard,
                                modifier = Modifier.clickable { selectedProjectType = type }
                            ) {
                                Text(
                                    text = type,
                                    color = if (isSelected) HetashreeBlack else HetashreeTextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // List of Casting Calls OR User Applications
        if (showMyApplicationsOnly) {
            if (userApplications.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                        Text("You haven't submitted any casting applications yet.", color = HetashreeTextSecondary, fontSize = 13.sp)
                    }
                }
            } else {
                items(userApplications, key = { it.id }) { app ->
                    ApplicationStatusCard(
                        application = app,
                        isProfessionalOrAdmin = isProfessionalOrAdmin,
                        onUpdateStatus = { newStatus -> onUpdateApplicationStatus(app.id, newStatus) }
                    )
                }
            }
        } else {
            items(filteredCalls, key = { it.id }) { call ->
                val hasApplied = applications.any { it.castingCallId == call.id && it.artistIdCode == userArtistId }
                CastingCallCard(
                    call = call,
                    hasApplied = hasApplied,
                    userArtistId = userArtistId,
                    onApply = { onApplyToCall(call.id) }
                )
            }
        }
    }
}

@Composable
private fun CastingModuleShortcut(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = HetashreeCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeBorder),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = HetashreeGoldPrimary, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = label, color = HetashreeTextPrimary, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
        }
    }
}

@Composable
private fun CastingCallCard(
    call: CastingCall,
    hasApplied: Boolean,
    userArtistId: String,
    onApply: () -> Unit
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = HetashreeCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .testTag("casting_call_${call.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Project Name & Budget
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = call.projectName,
                        color = HetashreeTextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${call.projectType} • ${call.productionHouse}",
                        color = HetashreeGoldLight,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = HetashreeGoldAmber.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeGoldPrimary)
                ) {
                    Text(
                        text = currencyFormat.format(call.budgetAmount),
                        color = HetashreeGoldBright,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Role Badge & Category
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = HetashreeCardElevated
                ) {
                    Text(
                        text = "ROLE: ${call.roleName}",
                        color = HetashreeTextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "${call.artistCategory} (${call.gender})",
                    color = HetashreeTextSecondary,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Criteria Details
            Text(
                text = "• Age: ${call.minAge}–${call.maxAge} yrs  • Height: ${call.minHeightCm}–${call.maxHeightCm} cm  • Shoot: ${call.workingDays} days",
                color = HetashreeTextSecondary,
                fontSize = 11.sp
            )
            Text(
                text = "• Location: ${call.location}  • Audition: ${call.auditionDate} (${call.auditionLocation})",
                color = HetashreeTextSecondary,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Project Description
            Text(
                text = call.projectDescription,
                color = HetashreeTextPrimary.copy(alpha = 0.85f),
                fontSize = 11.5.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Footer: Deadline & Apply Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Deadline: ${call.applicationDeadline}",
                    color = HetashreeTextMuted,
                    fontSize = 10.5.sp
                )

                if (hasApplied) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = HetashreeSuccess.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeSuccess)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = HetashreeSuccess, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Applied as $userArtistId", color = HetashreeSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Button(
                        onClick = onApply,
                        colors = ButtonDefaults.buttonColors(containerColor = HetashreeGoldPrimary),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("apply_call_${call.id}")
                    ) {
                        Text("Apply with $userArtistId", color = HetashreeBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ApplicationStatusCard(
    application: CastingApplication,
    isProfessionalOrAdmin: Boolean,
    onUpdateStatus: (ApplicationStatus) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = HetashreeCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = application.projectName, color = HetashreeTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Role: ${application.roleName} • Applied: ${application.appliedDate}", color = HetashreeTextSecondary, fontSize = 11.sp)
                }

                val statusColor = when (application.status) {
                    ApplicationStatus.APPLIED -> HetashreeTextSecondary
                    ApplicationStatus.UNDER_REVIEW -> HetashreeWarning
                    ApplicationStatus.SHORTLISTED -> HetashreeGoldBright
                    ApplicationStatus.SELECTED -> HetashreeSuccess
                    ApplicationStatus.REJECTED -> HetashreeError
                    ApplicationStatus.ON_HOLD -> Color.Gray
                    ApplicationStatus.COMPLETED -> HetashreeVerifiedBlue
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = statusColor.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, statusColor)
                ) {
                    Text(
                        text = application.status.label,
                        color = statusColor,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            if (application.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Casting Note: ${application.notes}",
                    color = HetashreeGoldLight,
                    fontSize = 11.sp
                )
            }

            if (isProfessionalOrAdmin) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Update Candidate Status:", color = HetashreeTextSecondary, fontSize = 10.sp)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ApplicationStatus.values().take(4).forEach { status ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (application.status == status) HetashreeGoldPrimary else HetashreeCardElevated,
                            modifier = Modifier.clickable { onUpdateStatus(status) }
                        ) {
                            Text(
                                text = status.label,
                                color = if (application.status == status) HetashreeBlack else HetashreeTextPrimary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

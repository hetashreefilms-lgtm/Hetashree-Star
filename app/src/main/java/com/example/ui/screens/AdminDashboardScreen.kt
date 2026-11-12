package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*

enum class AdminSection {
    OVERVIEW,
    ARTIST_VERIFICATION,
    CONTACT_REQUESTS,
    AUDIT_TRAIL
}

@Composable
fun AdminDashboardScreen(
    profiles: List<ArtistProfile>,
    castingCalls: List<CastingCall>,
    applications: List<CastingApplication>,
    agreements: List<Agreement>,
    payments: List<PaymentRecord>,
    contactRequests: List<ContactAccessRequest>,
    auditLogs: List<AuditLogEntry>,
    onBack: () -> Unit,
    onVerifyArtist: (String, VerificationStatus) -> Unit,
    onApproveContactRequest: (String, ContactRequestStatus) -> Unit
) {
    var activeSection by remember { mutableStateOf(AdminSection.OVERVIEW) }

    // Aggregate statistics
    val totalArtists = profiles.size
    val maleArtists = profiles.count { it.gender.equals("Male", ignoreCase = true) }
    val femaleArtists = profiles.count { it.gender.equals("Female", ignoreCase = true) }
    val kidsArtists = profiles.count { it.gender.equals("Kid", ignoreCase = true) || it.age < 18 }
    val pendingVerifications = profiles.filter { it.verificationStatus == VerificationStatus.PENDING }
    val approvedArtists = profiles.count { it.verificationStatus == VerificationStatus.APPROVED }
    val activeCallsCount = castingCalls.count { it.status == "Active" }
    val selectedCandidates = applications.count { it.status == ApplicationStatus.SELECTED }
    val pendingAgreements = agreements.count { it.status == AgreementStatus.PENDING || it.status == AgreementStatus.SENT }
    val completedAgreements = agreements.count { it.status == AgreementStatus.APPROVED || it.status == AgreementStatus.COMPLETED || it.status == AgreementStatus.SIGNED }
    val completedPayments = payments.count { it.status == "Completed" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(HetashreeBlack)
            .testTag("admin_dashboard_screen"),
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
                    IconButton(onClick = onBack, modifier = Modifier.testTag("admin_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = HetashreeTextPrimary)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Super Admin Command Center",
                                color = HetashreeGoldBright,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = HetashreeGoldAmber.copy(alpha = 0.25f)
                            ) {
                                Text(
                                    text = "SUPER_ADMIN",
                                    color = HetashreeGoldBright,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Primary Admin: hetashreefilms@gmail.com",
                            color = HetashreeTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Navigation Tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    AdminTabButton(
                        label = "Overview",
                        selected = activeSection == AdminSection.OVERVIEW,
                        modifier = Modifier.weight(1f)
                    ) { activeSection = AdminSection.OVERVIEW }

                    AdminTabButton(
                        label = "Verify (${pendingVerifications.size})",
                        selected = activeSection == AdminSection.ARTIST_VERIFICATION,
                        modifier = Modifier.weight(1f)
                    ) { activeSection = AdminSection.ARTIST_VERIFICATION }

                    AdminTabButton(
                        label = "Access (${contactRequests.size})",
                        selected = activeSection == AdminSection.CONTACT_REQUESTS,
                        modifier = Modifier.weight(1f)
                    ) { activeSection = AdminSection.CONTACT_REQUESTS }

                    AdminTabButton(
                        label = "Audit",
                        selected = activeSection == AdminSection.AUDIT_TRAIL,
                        modifier = Modifier.weight(1f)
                    ) { activeSection = AdminSection.AUDIT_TRAIL }
                }
            }
            HorizontalDivider(color = HetashreeBorder)
        }

        // Section Content
        when (activeSection) {
            AdminSection.OVERVIEW -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Platform Analytics & Production KPIs",
                            color = HetashreeTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )

                        // 4-card metric grid
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            MetricCard(title = "Total Artists", value = "$totalArtists", subtitle = "Male: $maleArtists | Fem: $femaleArtists | Kids: $kidsArtists", modifier = Modifier.weight(1f))
                            MetricCard(title = "Active Castings", value = "$activeCallsCount", subtitle = "Total Apps: ${applications.size}", modifier = Modifier.weight(1f))
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            MetricCard(title = "Agreements", value = "$completedAgreements", subtitle = "Pending Review: $pendingAgreements", modifier = Modifier.weight(1f))
                            MetricCard(title = "Completed Payouts", value = "$completedPayments", subtitle = "25% Agency Commission", modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Admin actions overview
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = HetashreeCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Administrative Policy Enforcement", color = HetashreeGoldBright, fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("• All talent contact info is guarded under Zero-Exposure privacy rules.", color = HetashreeTextSecondary, fontSize = 11.sp)
                                Text("• Unique sequential Artist IDs (HT-ART-00000X) are non-recyclable.", color = HetashreeTextSecondary, fontSize = 11.sp)
                                Text("• Contracts adhere strictly to 25% default agency commission and ₹100 stamp charge.", color = HetashreeTextSecondary, fontSize = 11.sp)
                                Text("• Super Admin account hetashreefilms@gmail.com maintains master audit logs.", color = HetashreeTextSecondary, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            AdminSection.ARTIST_VERIFICATION -> {
                item {
                    Text(
                        text = "Artist Verification Queue (${profiles.size})",
                        color = HetashreeTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(14.dp)
                    )
                }

                items(profiles, key = { it.artistIdCode }) { profile ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = HetashreeCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 5.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(profile.fullName, color = HetashreeTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                                    Text("${profile.artistIdCode} • ${profile.primaryCategory} • ${profile.age}y", color = HetashreeGoldLight, fontSize = 11.sp)
                                }

                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (profile.verificationStatus == VerificationStatus.APPROVED) HetashreeSuccess.copy(alpha = 0.2f) else HetashreeWarning.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = profile.verificationStatus.name,
                                        color = if (profile.verificationStatus == VerificationStatus.APPROVED) HetashreeSuccess else HetashreeWarning,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { onVerifyArtist(profile.artistIdCode, VerificationStatus.APPROVED) },
                                    colors = ButtonDefaults.buttonColors(containerColor = HetashreeSuccess),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Approve & Verify", color = Color.White, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = { onVerifyArtist(profile.artistIdCode, VerificationStatus.REJECTED) },
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Reject", color = HetashreeError, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            AdminSection.CONTACT_REQUESTS -> {
                item {
                    Text(
                        text = "Contact Access Requests (${contactRequests.size})",
                        color = HetashreeTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(14.dp)
                    )
                }

                items(contactRequests, key = { it.id }) { req ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = HetashreeCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 5.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Requester: ${req.professionalName} (${req.professionalCompany})",
                                    color = HetashreeTextPrimary,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(text = req.status.name, color = HetashreeGoldBright, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Text("Target Artist: ${req.artistName} (${req.artistIdCode})", color = HetashreeGoldLight, fontSize = 11.5.sp)
                            Text("Reason: ${req.reason}", color = HetashreeTextSecondary, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(8.dp))

                            if (req.status == ContactRequestStatus.PENDING) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = { onApproveContactRequest(req.id, ContactRequestStatus.APPROVED) },
                                        colors = ButtonDefaults.buttonColors(containerColor = HetashreeGoldPrimary),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Grant Contact Access", color = HetashreeBlack, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                                    }

                                    OutlinedButton(
                                        onClick = { onApproveContactRequest(req.id, ContactRequestStatus.REJECTED) },
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Deny Access", color = HetashreeTextSecondary, fontSize = 10.5.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            AdminSection.AUDIT_TRAIL -> {
                item {
                    Text(
                        text = "Real-Time Administrative Audit Logs (${auditLogs.size})",
                        color = HetashreeTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(14.dp)
                    )
                }

                items(auditLogs, key = { it.id }) { log ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = HetashreeCard,
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, HetashreeBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = log.action, color = HetashreeGoldPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(text = log.timestamp, color = HetashreeTextMuted, fontSize = 10.sp)
                            }
                            Text(text = "Actor: ${log.actorName} (${log.actorRole})", color = HetashreeTextSecondary, fontSize = 10.5.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = log.details, color = HetashreeTextPrimary, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminTabButton(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = if (selected) HetashreeGoldPrimary else HetashreeCard,
        modifier = modifier.clickable { onClick() }
    ) {
        Box(modifier = Modifier.padding(vertical = 7.dp), contentAlignment = Alignment.Center) {
            Text(
                text = label,
                color = if (selected) HetashreeBlack else HetashreeTextPrimary,
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = HetashreeCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeBorder),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, color = HetashreeTextSecondary, fontSize = 10.5.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, color = HetashreeGoldBright, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, color = HetashreeTextMuted, fontSize = 9.5.sp, maxLines = 1)
        }
    }
}

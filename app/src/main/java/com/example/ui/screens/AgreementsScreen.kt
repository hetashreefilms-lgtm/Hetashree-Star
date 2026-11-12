package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.model.Agreement
import com.example.model.AgreementStatus
import com.example.model.User
import com.example.model.UserRole
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AgreementsScreen(
    currentUser: User,
    agreements: List<Agreement>,
    onBack: () -> Unit,
    onSignAsArtist: (String) -> Unit,
    onApproveByAdmin: (String) -> Unit
) {
    var selectedAgreement by remember { mutableStateOf<Agreement?>(null) }
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(HetashreeBlack)
            .testTag("agreements_screen"),
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
                    IconButton(onClick = onBack, modifier = Modifier.testTag("agreements_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = HetashreeTextPrimary)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Legal Production Agreements",
                            color = HetashreeGoldBright,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Parties: Producer / Client & HETASHREE FILM'S PRODUCTION",
                            color = HetashreeTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Transparent Stamp Paper / Processing Notice
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = HetashreeGoldAmber.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeGoldAmber.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = HetashreeGoldBright, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Configurable ₹100 Stamp-Paper / Processing Charge",
                                color = HetashreeGoldBright,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Transparent legal administration charge per executed contract. This is strictly NOT a registration or joining fee.",
                                color = HetashreeTextSecondary,
                                fontSize = 10.5.sp
                            )
                        }
                    }
                }
            }
            HorizontalDivider(color = HetashreeBorder)
        }

        // List of Agreements
        if (agreements.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    Text("No agreements active at this time.", color = HetashreeTextSecondary)
                }
            }
        } else {
            items(agreements, key = { it.id }) { agreement ->
                AgreementCard(
                    agreement = agreement,
                    currentUser = currentUser,
                    currencyFormat = currencyFormat,
                    onViewDetails = { selectedAgreement = agreement },
                    onSignAsArtist = { onSignAsArtist(agreement.id) },
                    onApproveByAdmin = { onApproveByAdmin(agreement.id) }
                )
            }
        }
    }

    // Detail Modal / Sheet
    selectedAgreement?.let { agreement ->
        AgreementDetailDialog(
            agreement = agreement,
            currencyFormat = currencyFormat,
            onDismiss = { selectedAgreement = null }
        )
    }
}

@Composable
private fun AgreementCard(
    agreement: Agreement,
    currentUser: User,
    currencyFormat: NumberFormat,
    onViewDetails: () -> Unit,
    onSignAsArtist: () -> Unit,
    onApproveByAdmin: () -> Unit
) {
    val statusColor = when (agreement.status) {
        AgreementStatus.DRAFT -> HetashreeTextSecondary
        AgreementStatus.PENDING -> HetashreeWarning
        AgreementStatus.SENT -> HetashreeGoldBright
        AgreementStatus.SIGNED -> HetashreeGoldAmber
        AgreementStatus.APPROVED -> HetashreeSuccess
        AgreementStatus.REJECTED -> HetashreeError
        AgreementStatus.COMPLETED -> HetashreeVerifiedBlue
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = HetashreeCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .testTag("agreement_card_${agreement.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = agreement.id, color = HetashreeGoldPrimary, fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                    Text(text = agreement.projectName, color = HetashreeTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = statusColor.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, statusColor)
                ) {
                    Text(
                        text = agreement.status.label,
                        color = statusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Producer: ${agreement.producerName} (${agreement.productionHouse})",
                color = HetashreeTextSecondary,
                fontSize = 11.5.sp
            )
            Text(
                text = "Artist: ${agreement.artistName} (${agreement.artistIdCode})",
                color = HetashreeTextSecondary,
                fontSize = 11.5.sp
            )
            Text(
                text = "Location: ${agreement.shootingLocation} • Shoot: ${agreement.workingDays} working days",
                color = HetashreeTextMuted,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Financial Summary Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HetashreeDarkSurface, RoundedCornerShape(6.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Total Payment", color = HetashreeTextSecondary, fontSize = 10.sp)
                    Text(currencyFormat.format(agreement.artistPayment), color = HetashreeTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("Agency Comm.", color = HetashreeTextSecondary, fontSize = 10.sp)
                    Text("${agreement.agencyCommissionPercent.toInt()}%", color = HetashreeGoldBright, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("Processing Charge", color = HetashreeTextSecondary, fontSize = 10.sp)
                    Text(currencyFormat.format(agreement.processingCharge), color = HetashreeGoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Signature status indicators
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SigBadge(label = "Producer", signed = agreement.signedByProducer)
                    SigBadge(label = "Artist", signed = agreement.signedByArtist)
                    SigBadge(label = "HETASHREE", signed = agreement.verifiedByAdmin)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    TextButton(onClick = onViewDetails) {
                        Text("View Terms", color = HetashreeGoldBright, fontSize = 11.sp)
                    }

                    // Artist Sign Action
                    if (!agreement.signedByArtist && (currentUser.artistIdCode == agreement.artistIdCode || currentUser.role == UserRole.ARTIST)) {
                        Button(
                            onClick = onSignAsArtist,
                            colors = ButtonDefaults.buttonColors(containerColor = HetashreeGoldPrimary),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("sign_agreement_btn")
                        ) {
                            Text("Sign (₹100)", color = HetashreeBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Admin Approve Action
                    if (!agreement.verifiedByAdmin && (currentUser.role == UserRole.SUPER_ADMIN || currentUser.role == UserRole.AUTHORIZED_ADMIN)) {
                        Button(
                            onClick = onApproveByAdmin,
                            colors = ButtonDefaults.buttonColors(containerColor = HetashreeSuccess),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("admin_approve_agreement_btn")
                        ) {
                            Text("Admin Verify", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SigBadge(label: String, signed: Boolean) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = if (signed) HetashreeSuccess.copy(alpha = 0.15f) else HetashreeDarkSurface
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (signed) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (signed) HetashreeSuccess else HetashreeTextMuted,
                modifier = Modifier.size(11.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(label, color = if (signed) HetashreeSuccess else HetashreeTextMuted, fontSize = 9.5.sp)
        }
    }
}

@Composable
private fun AgreementDetailDialog(
    agreement: Agreement,
    currencyFormat: NumberFormat,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = HetashreeDarkSurface,
        title = {
            Column {
                Text("HETASHREE FILM'S PRODUCTION", color = HetashreeGoldBright, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("Official Production Contract • ${agreement.id}", color = HetashreeTextSecondary, fontSize = 11.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 350.dp)
            ) {
                Text("Project: ${agreement.projectName} (${agreement.projectCategory})", color = HetashreeTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("Parties: ${agreement.producerName} & HETASHREE FILM'S PRODUCTION", color = HetashreeTextSecondary, fontSize = 11.5.sp)
                Text("Artist: ${agreement.artistName} (${agreement.artistIdCode})", color = HetashreeTextSecondary, fontSize = 11.5.sp)

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = HetashreeBorder)
                Spacer(modifier = Modifier.height(8.dp))

                Text("Payment Schedule:", color = HetashreeGoldLight, fontWeight = FontWeight.SemiBold, fontSize = 11.5.sp)
                Text(agreement.paymentTerms, color = HetashreeTextSecondary, fontSize = 11.sp)

                Spacer(modifier = Modifier.height(6.dp))
                Text("Agency Commission:", color = HetashreeGoldLight, fontWeight = FontWeight.SemiBold, fontSize = 11.5.sp)
                Text("${agreement.agencyCommissionPercent.toInt()}% agency commission on gross artist contract amount as per HETASHREE production guild rules.", color = HetashreeTextSecondary, fontSize = 11.sp)

                Spacer(modifier = Modifier.height(6.dp))
                Text("Processing Charge:", color = HetashreeGoldLight, fontWeight = FontWeight.SemiBold, fontSize = 11.5.sp)
                Text("₹100 legal stamp/processing fee applicable upon formal digital signature.", color = HetashreeTextSecondary, fontSize = 11.sp)

                Spacer(modifier = Modifier.height(6.dp))
                Text("Confidentiality (NDA):", color = HetashreeGoldLight, fontWeight = FontWeight.SemiBold, fontSize = 11.5.sp)
                Text(agreement.confidentialityTerms, color = HetashreeTextSecondary, fontSize = 11.sp)
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = HetashreeGoldPrimary)
            ) {
                Text("Close Document", color = HetashreeBlack, fontWeight = FontWeight.Bold)
            }
        }
    )
}

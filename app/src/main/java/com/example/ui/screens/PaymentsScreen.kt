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
import com.example.model.PaymentRecord
import com.example.model.User
import com.example.model.UserRole
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PaymentsScreen(
    currentUser: User,
    payments: List<PaymentRecord>,
    onBack: () -> Unit,
    onRecordNewPayment: (
        agreementId: String,
        projectName: String,
        producerName: String,
        artistName: String,
        artistIdCode: String,
        producerPayment: Double,
        commissionPercent: Double,
        method: String,
        ref: String
    ) -> Unit
) {
    var selectedPaymentForReceipt by remember { mutableStateOf<PaymentRecord?>(null) }
    var showRecordPaymentDialog by remember { mutableStateOf(false) }
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))

    val isAuthorized = currentUser.role in listOf(
        UserRole.SUPER_ADMIN,
        UserRole.AUTHORIZED_ADMIN,
        UserRole.PRODUCER,
        UserRole.CASTING_DIRECTOR
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(HetashreeBlack)
            .testTag("payments_screen"),
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("payments_back_btn")) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = HetashreeTextPrimary)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text = "Production Payments & Commission",
                                color = HetashreeGoldBright,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Transparent 25% Casting Agency Commission System",
                                color = HetashreeTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    if (isAuthorized) {
                        Button(
                            onClick = { showRecordPaymentDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = HetashreeGoldPrimary),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("record_payment_btn")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = HetashreeBlack, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("Record Payout", color = HetashreeBlack, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Commission Model Banner
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = HetashreeCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Calculate, contentDescription = null, tint = HetashreeGoldPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Standard HETASHREE Commission Structure (25%)",
                                color = HetashreeGoldBright,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Example: Project Amount ₹50,000 → 25% Agency Commission ₹12,500 → Net Artist Payout ₹37,500. Fully audited with direct bank receipt verification.",
                            color = HetashreeTextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
            HorizontalDivider(color = HetashreeBorder)
        }

        // Transactions List
        item {
            Text(
                text = "Verified Payment Records (${payments.size})",
                color = HetashreeTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
            )
        }

        items(payments, key = { it.id }) { record ->
            PaymentRecordCard(
                record = record,
                currencyFormat = currencyFormat,
                onViewReceipt = { selectedPaymentForReceipt = record }
            )
        }
    }

    // Receipt Dialog
    selectedPaymentForReceipt?.let { payment ->
        PaymentReceiptDialog(
            record = payment,
            currencyFormat = currencyFormat,
            onDismiss = { selectedPaymentForReceipt = null }
        )
    }

    // Record Payment Dialog
    if (showRecordPaymentDialog) {
        RecordPaymentDialog(
            onDismiss = { showRecordPaymentDialog = false },
            onConfirm = { agrId, proj, prod, art, code, amount, comm, meth, ref ->
                onRecordNewPayment(agrId, proj, prod, art, code, amount, comm, meth, ref)
                showRecordPaymentDialog = false
            }
        )
    }
}

@Composable
private fun PaymentRecordCard(
    record: PaymentRecord,
    currencyFormat: NumberFormat,
    onViewReceipt: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = HetashreeCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .testTag("payment_record_${record.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = record.id, color = HetashreeGoldPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = record.projectName, color = HetashreeTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = HetashreeSuccess.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeSuccess)
                ) {
                    Text(
                        text = record.status,
                        color = HetashreeSuccess,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Artist: ${record.artistName} (${record.artistIdCode}) • Producer: ${record.producerName}",
                color = HetashreeTextSecondary,
                fontSize = 11.5.sp
            )
            Text(
                text = "Paid via: ${record.paymentMethod} • Date: ${record.paymentDate}",
                color = HetashreeTextMuted,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Transparent Breakdown Grid
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = HetashreeDarkSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    BreakdownRow("Gross Producer Amount:", currencyFormat.format(record.producerPaymentAmount), HetashreeTextPrimary)
                    BreakdownRow("Agency Commission (${record.agencyCommissionPercent.toInt()}%):", "- ${currencyFormat.format(record.agencyCommissionAmount)}", HetashreeGoldAmber)
                    HorizontalDivider(color = HetashreeBorder, thickness = 0.5.dp)
                    BreakdownRow("Net Artist Amount:", currencyFormat.format(record.artistPayableAmount), HetashreeGoldBright, isBold = true)
                    BreakdownRow("Advance Disbursed:", currencyFormat.format(record.advancePaid), HetashreeSuccess)
                    BreakdownRow("Remaining Balance:", currencyFormat.format(record.remainingAmount), HetashreeTextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ref: ${record.transactionRef}",
                    color = HetashreeTextMuted,
                    fontSize = 10.5.sp
                )

                TextButton(
                    onClick = onViewReceipt,
                    modifier = Modifier.testTag("view_receipt_${record.id}")
                ) {
                    Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = HetashreeGoldPrimary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("View Official Receipt", color = HetashreeGoldPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun BreakdownRow(label: String, value: String, color: Color, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = HetashreeTextSecondary, fontSize = 11.sp)
        Text(text = value, color = color, fontSize = 11.5.sp, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium)
    }
}

@Composable
private fun PaymentReceiptDialog(
    record: PaymentRecord,
    currencyFormat: NumberFormat,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = HetashreeDarkSurface,
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("HETASHREE FILM'S PRODUCTION", color = HetashreeGoldBright, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("HETASHREE CASTING & TALENT DIVISION", color = HetashreeGoldLight, fontSize = 10.sp)
                Text("OFFICIAL PAYMENT DISBURSEMENT RECEIPT", color = HetashreeTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HetashreeCard, RoundedCornerShape(8.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ReceiptLine("Receipt ID:", record.id)
                ReceiptLine("Agreement ID:", record.agreementId)
                ReceiptLine("Project Name:", record.projectName)
                ReceiptLine("Producer:", record.producerName)
                ReceiptLine("Artist:", "${record.artistName} (${record.artistIdCode})")
                HorizontalDivider(color = HetashreeBorder)
                ReceiptLine("Gross Contract Amount:", currencyFormat.format(record.producerPaymentAmount))
                ReceiptLine("Agency Commission (25%):", currencyFormat.format(record.agencyCommissionAmount))
                ReceiptLine("Net Artist Payout:", currencyFormat.format(record.artistPayableAmount))
                ReceiptLine("Disbursed Advance:", currencyFormat.format(record.advancePaid))
                ReceiptLine("Remaining Payout:", currencyFormat.format(record.remainingAmount))
                ReceiptLine("Transaction Reference:", record.transactionRef)
                ReceiptLine("Date of Clearance:", record.paymentDate)
                ReceiptLine("Audit Verification:", record.receiptNotes)
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = HetashreeGoldPrimary)
            ) {
                Text("Close Receipt", color = HetashreeBlack, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun ReceiptLine(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = HetashreeTextSecondary, fontSize = 10.5.sp)
        Text(text = value, color = HetashreeTextPrimary, fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun RecordPaymentDialog(
    onDismiss: () -> Unit,
    onConfirm: (
        agreementId: String,
        projectName: String,
        producerName: String,
        artistName: String,
        artistIdCode: String,
        producerPayment: Double,
        commissionPercent: Double,
        method: String,
        ref: String
    ) -> Unit
) {
    var projectName by remember { mutableStateOf("Sparkle Gold Festive TVC") }
    var producerName by remember { mutableStateOf("Kabir Mehta") }
    var artistName by remember { mutableStateOf("Priya Sharma") }
    var artistIdCode by remember { mutableStateOf("HT-ART-000001") }
    var agreementId by remember { mutableStateOf("HT-AGR-2026-002") }
    var grossAmount by remember { mutableStateOf("180000") }
    var commission by remember { mutableStateOf("25") }
    var method by remember { mutableStateOf("Bank Transfer (NEFT/RTGS)") }
    var ref by remember { mutableStateOf("AXIS-N883011928") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = HetashreeDarkSurface,
        title = {
            Text("Record Producer Payment & Artist Payout", color = HetashreeGoldBright, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 350.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedTextField(value = projectName, onValueChange = { projectName = it }, label = { Text("Project Title") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = producerName, onValueChange = { producerName = it }, label = { Text("Producer / Client") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = artistName, onValueChange = { artistName = it }, label = { Text("Artist Full Name") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = artistIdCode, onValueChange = { artistIdCode = it }, label = { Text("Artist ID (HT-ART-...)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = grossAmount, onValueChange = { grossAmount = it }, label = { Text("Gross Producer Amount (₹)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = ref, onValueChange = { ref = it }, label = { Text("Bank Reference / Cheque Ref") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = grossAmount.toDoubleOrNull() ?: 50000.0
                    val comm = commission.toDoubleOrNull() ?: 25.0
                    onConfirm(agreementId, projectName, producerName, artistName, artistIdCode, amount, comm, method, ref)
                },
                colors = ButtonDefaults.buttonColors(containerColor = HetashreeGoldPrimary)
            ) {
                Text("Confirm Disbursement", color = HetashreeBlack, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = HetashreeTextSecondary)
            }
        }
    )
}

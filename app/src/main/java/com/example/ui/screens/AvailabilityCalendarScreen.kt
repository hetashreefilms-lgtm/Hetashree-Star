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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AvailabilityEntry
import com.example.model.User
import com.example.ui.theme.*

@Composable
fun AvailabilityCalendarScreen(
    currentUser: User,
    availability: List<AvailabilityEntry>,
    onBack: () -> Unit,
    onAddEntry: (AvailabilityEntry) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    val userArtistId = currentUser.artistIdCode ?: "HT-ART-000001"
    val userEntries = availability.filter { it.artistIdCode == userArtistId }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(HetashreeBlack)
            .testTag("availability_calendar_screen"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
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
                        IconButton(onClick = onBack, modifier = Modifier.testTag("calendar_back_btn")) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = HetashreeTextPrimary)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text = "Artist Availability Calendar",
                                color = HetashreeGoldBright,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Calendar for $userArtistId • Avoid Double Booking",
                                color = HetashreeTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Button(
                        onClick = { showAddDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = HetashreeGoldPrimary),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("add_calendar_entry_btn")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = HetashreeBlack, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Add Date", color = HetashreeBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            HorizontalDivider(color = HetashreeBorder)
        }

        item {
            Text(
                text = "Scheduled Commitments & Shooting Blocks (${userEntries.size})",
                color = HetashreeTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
            )
        }

        if (userEntries.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    Text("No scheduled dates recorded for $userArtistId.", color = HetashreeTextSecondary)
                }
            }
        } else {
            items(userEntries, key = { it.id }) { entry ->
                AvailabilityItemCard(entry)
            }
        }
    }

    if (showAddDialog) {
        AddAvailabilityDialog(
            artistIdCode = userArtistId,
            onDismiss = { showAddDialog = false },
            onConfirm = { entry ->
                onAddEntry(entry)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun AvailabilityItemCard(entry: AvailabilityEntry) {
    val (typeColor, icon) = when (entry.type) {
        "Available" -> HetashreeSuccess to Icons.Default.EventAvailable
        "Audition" -> HetashreeGoldBright to Icons.Default.CalendarMonth
        "Shoot" -> HetashreeGoldAmber to Icons.Default.Movie
        else -> HetashreeError to Icons.Default.EventBusy
    }

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = HetashreeCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, HetashreeBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 5.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(typeColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = typeColor, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = entry.date, color = HetashreeTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = typeColor.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = entry.type,
                            color = typeColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(text = entry.note, color = HetashreeTextSecondary, fontSize = 11.5.sp)
            }
        }
    }
}

@Composable
private fun AddAvailabilityDialog(
    artistIdCode: String,
    onDismiss: () -> Unit,
    onConfirm: (AvailabilityEntry) -> Unit
) {
    var date by remember { mutableStateOf("2026-12-15") }
    var type by remember { mutableStateOf("Shoot") }
    var note by remember { mutableStateOf("Feature Film Principal Shoot") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = HetashreeDarkSurface,
        title = { Text("Add Availability Commitment", color = HetashreeGoldBright, fontSize = 14.sp, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Type (Available/Shoot/Audition/Leave)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Note / Project Details") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val entry = AvailabilityEntry(
                        id = "AVL-${System.currentTimeMillis()}",
                        artistIdCode = artistIdCode,
                        date = date,
                        type = type,
                        note = note
                    )
                    onConfirm(entry)
                },
                colors = ButtonDefaults.buttonColors(containerColor = HetashreeGoldPrimary)
            ) {
                Text("Save Date", color = HetashreeBlack, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = HetashreeTextSecondary) }
        }
    )
}

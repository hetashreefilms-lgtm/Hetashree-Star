package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.ArtistProfile
import com.example.ui.theme.*

@Composable
fun ContactAccessDialog(
    profile: ArtistProfile,
    onDismiss: () -> Unit,
    onSubmitRequest: (reason: String) -> Unit
) {
    var reason by remember { mutableStateOf("Direct contract discussions for upcoming feature film role.") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = HetashreeDarkSurface,
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth().testTag("contact_access_dialog")
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Private Data",
                        tint = HetashreeGoldAmber,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Private Contact Access",
                        color = HetashreeGoldBright,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Requesting verified direct contact info for ${profile.fullName} (${profile.artistIdCode}).",
                    color = HetashreeTextPrimary,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = HetashreeCard,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "Security policy",
                                tint = HetashreeGoldLight,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "HETASHREE Privacy & Audit Policy",
                                color = HetashreeGoldLight,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "To safeguard talent privacy, direct mobile numbers, personal emails, and home addresses are never public. Access requires explicit artist consent and is logged in administrative audit trails.",
                            color = HetashreeTextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Purpose / Project Reason") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = HetashreeTextPrimary,
                        unfocusedTextColor = HetashreeTextPrimary,
                        focusedBorderColor = HetashreeGoldPrimary,
                        unfocusedBorderColor = HetashreeBorder
                    ),
                    modifier = Modifier.fillMaxWidth().height(100.dp).testTag("contact_reason_input"),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = HetashreeTextSecondary)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (reason.isNotBlank()) {
                                onSubmitRequest(reason)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = HetashreeGoldPrimary),
                        modifier = Modifier.testTag("submit_contact_request_button")
                    ) {
                        Text("Send Request", color = HetashreeBlack, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

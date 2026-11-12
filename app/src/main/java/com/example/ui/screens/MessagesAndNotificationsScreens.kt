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
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.model.ChatMessage
import com.example.model.ChatThread
import com.example.model.NotificationItem
import com.example.model.User
import com.example.ui.theme.*

@Composable
fun MessagesScreen(
    currentUser: User,
    threads: List<ChatThread>,
    messages: List<ChatMessage>,
    onBack: () -> Unit,
    onSendMessage: (receiverId: String, text: String) -> Unit
) {
    var activeThread by remember { mutableStateOf<ChatThread?>(null) }
    var messageInput by remember { mutableStateOf("") }

    if (activeThread != null) {
        // 1-on-1 Chat Detail
        val thread = activeThread!!
        val threadMessages = messages.filter {
            (it.senderId == currentUser.id && it.receiverId == thread.participantId) ||
                    (it.senderId == thread.participantId && it.receiverId == currentUser.id)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(HetashreeBlack)
                .statusBarsPadding()
                .navigationBarsPadding()
                .testTag("chat_detail_screen")
        ) {
            // Chat Header
            Surface(
                color = HetashreeDarkSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { activeThread = null }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = HetashreeTextPrimary)
                    }

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .border(1.dp, HetashreeGoldPrimary, CircleShape)
                    ) {
                        AsyncImage(
                            model = thread.participantAvatar,
                            contentDescription = thread.participantName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = thread.participantName,
                            color = HetashreeTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${thread.participantRole} • Active Now",
                            color = HetashreeGoldLight,
                            fontSize = 10.5.sp
                        )
                    }
                }
            }

            HorizontalDivider(color = HetashreeBorder)

            // Message list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(threadMessages) { msg ->
                    val isMe = msg.senderId == currentUser.id
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isMe) HetashreeGoldPrimary else HetashreeCardElevated,
                            modifier = Modifier.widthIn(max = 280.dp)
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                Text(
                                    text = msg.messageText,
                                    color = if (isMe) HetashreeBlack else HetashreeTextPrimary,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = msg.timestamp,
                                    color = if (isMe) HetashreeBlack.copy(alpha = 0.6f) else HetashreeTextMuted,
                                    fontSize = 9.sp,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                    }
                }
            }

            // Message Input
            Surface(
                color = HetashreeDarkSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = messageInput,
                        onValueChange = { messageInput = it },
                        placeholder = { Text("Type professional message...", color = HetashreeTextMuted, fontSize = 12.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = HetashreeTextPrimary,
                            unfocusedTextColor = HetashreeTextPrimary,
                            focusedBorderColor = HetashreeGoldPrimary,
                            unfocusedBorderColor = HetashreeBorder,
                            focusedContainerColor = HetashreeCard,
                            unfocusedContainerColor = HetashreeCard
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.weight(1f),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (messageInput.isNotBlank()) {
                                onSendMessage(thread.participantId, messageInput.trim())
                                messageInput = ""
                            }
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(HetashreeGoldPrimary)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = HetashreeBlack, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    } else {
        // Threads List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(HetashreeBlack)
                .testTag("messages_screen"),
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
                        IconButton(onClick = onBack, modifier = Modifier.testTag("messages_back_btn")) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = HetashreeTextPrimary)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text = "Direct Messages",
                                color = HetashreeGoldBright,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Secure One-to-One Talent Communication",
                                color = HetashreeTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
                HorizontalDivider(color = HetashreeBorder)
            }

            items(threads, key = { it.participantId }) { thread ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = HetashreeCard,
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, HetashreeBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                        .clickable { activeThread = thread }
                        .testTag("chat_thread_${thread.participantId}")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .border(1.dp, HetashreeGoldPrimary, CircleShape)
                        ) {
                            AsyncImage(
                                model = thread.participantAvatar,
                                contentDescription = thread.participantName,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(thread.participantName, color = HetashreeTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                                Text(thread.lastMessageTime, color = HetashreeTextMuted, fontSize = 10.sp)
                            }
                            Text(thread.participantRole, color = HetashreeGoldLight, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(thread.lastMessage, color = HetashreeTextSecondary, fontSize = 12.sp, maxLines = 1)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem>,
    onBack: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Casting", "Social", "Agreement", "Payment", "Contact")

    val filtered = remember(notifications, selectedFilter) {
        if (selectedFilter == "All") notifications
        else notifications.filter { it.type.equals(selectedFilter, ignoreCase = true) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(HetashreeBlack)
            .testTag("notifications_screen"),
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
                    IconButton(onClick = onBack, modifier = Modifier.testTag("notifications_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = HetashreeTextPrimary)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Notifications",
                        color = HetashreeGoldBright,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    filters.forEach { f ->
                        val isSel = selectedFilter == f
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSel) HetashreeGoldPrimary else HetashreeCard,
                            modifier = Modifier.clickable { selectedFilter = f }
                        ) {
                            Text(
                                text = f,
                                color = if (isSel) HetashreeBlack else HetashreeTextPrimary,
                                fontSize = 11.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
            HorizontalDivider(color = HetashreeBorder)
        }

        items(filtered, key = { it.id }) { item ->
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = HetashreeCard,
                border = androidx.compose.foundation.BorderStroke(0.5.dp, HetashreeBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(HetashreeGoldAmber.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = HetashreeGoldBright, modifier = Modifier.size(18.dp))
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(item.title, color = HetashreeTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(item.timeAgo, color = HetashreeTextMuted, fontSize = 10.sp)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(item.message, color = HetashreeTextSecondary, fontSize = 11.5.sp)
                    }
                }
            }
        }
    }
}

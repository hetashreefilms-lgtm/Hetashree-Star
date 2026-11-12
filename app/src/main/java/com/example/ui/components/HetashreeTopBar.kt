package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.model.User
import com.example.model.UserRole
import com.example.ui.theme.*
import com.example.viewmodel.AppDestination

@Composable
fun HetashreeTopBar(
    currentUser: User,
    currentDestination: AppDestination,
    unreadNotifications: Int,
    unreadMessages: Int,
    onNavigate: (AppDestination) -> Unit
) {
    Surface(
        color = HetashreeBlack,
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo & Brand Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onNavigate(AppDestination.SOCIAL_FEED) }
                        .testTag("topbar_brand_logo")
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .border(1.dp, HetashreeGoldPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_hetashree_logo),
                            contentDescription = "HETASHREE STAR Logo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "HETASHREE",
                                color = HetashreeGoldBright,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "STAR",
                                color = HetashreeGoldAmber,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                letterSpacing = 1.2.sp
                            )
                        }
                        Text(
                            text = "DREAM • CAST • CREATE",
                            color = HetashreeTextSecondary,
                            fontWeight = FontWeight.Medium,
                            fontSize = 8.5.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Action Icons (Casting Mode Switcher, Notifications, Messages, User Switcher)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Casting Hub Quick Switcher
                    val isCastingMode = currentDestination in listOf(
                        AppDestination.CASTING_HUB,
                        AppDestination.ARTIST_SEARCH,
                        AppDestination.AGREEMENTS,
                        AppDestination.PAYMENTS,
                        AppDestination.CALENDAR,
                        AppDestination.ADMIN_PORTAL
                    )

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isCastingMode) HetashreeGoldPrimary else HetashreeCardElevated,
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .clickable {
                                if (isCastingMode) {
                                    onNavigate(AppDestination.SOCIAL_FEED)
                                } else {
                                    onNavigate(AppDestination.CASTING_HUB)
                                }
                            }
                            .testTag("mode_switcher_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = if (isCastingMode) Icons.Default.Stars else Icons.Default.BusinessCenter,
                                contentDescription = "Mode Switch",
                                tint = if (isCastingMode) HetashreeBlack else HetashreeGoldPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isCastingMode) "Social" else "Casting",
                                color = if (isCastingMode) HetashreeBlack else HetashreeGoldBright,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Search Icon
                    IconButton(
                        onClick = { onNavigate(AppDestination.EXPLORE_DISCOVER) },
                        modifier = Modifier.size(36.dp).testTag("topbar_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = HetashreeTextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Notifications with badge
                    IconButton(
                        onClick = { onNavigate(AppDestination.NOTIFICATIONS) },
                        modifier = Modifier.size(36.dp).testTag("topbar_notifications_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadNotifications > 0) {
                                    Badge(containerColor = HetashreeLikeRed) {
                                        Text("$unreadNotifications", color = Color.White, fontSize = 9.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsNone,
                                contentDescription = "Notifications",
                                tint = HetashreeTextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Direct Messages with badge
                    IconButton(
                        onClick = { onNavigate(AppDestination.MESSAGES) },
                        modifier = Modifier.size(36.dp).testTag("topbar_messages_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadMessages > 0) {
                                    Badge(containerColor = HetashreeGoldAmber) {
                                        Text("$unreadMessages", color = HetashreeBlack, fontSize = 9.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChatBubbleOutline,
                                contentDescription = "Messages",
                                tint = HetashreeTextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Switch Account / Login button
                    IconButton(
                        onClick = { onNavigate(AppDestination.AUTH_AND_SWITCHER) },
                        modifier = Modifier.size(36.dp).testTag("topbar_switch_user_button")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .border(1.dp, HetashreeGoldPrimary, CircleShape)
                        ) {
                            AsyncImage(
                                model = currentUser.avatarUrl,
                                contentDescription = "Active user avatar",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

            // Sub-indicator if professional or admin logged in
            if (currentUser.role != UserRole.ARTIST && currentUser.role != UserRole.KIDS_ARTIST) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Signed in as: ${currentUser.name} (${currentUser.role.displayName})",
                        color = HetashreeGoldLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    if (currentUser.role == UserRole.SUPER_ADMIN) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = HetashreeGoldAmber.copy(alpha = 0.2f),
                            modifier = Modifier.clickable { onNavigate(AppDestination.ADMIN_PORTAL) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = "Admin",
                                    tint = HetashreeGoldBright,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text("Admin Panel", color = HetashreeGoldBright, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

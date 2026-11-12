package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Send
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
import com.example.model.SocialPost
import com.example.model.StoryItem
import com.example.model.User
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    currentUser: User,
    stories: List<StoryItem>,
    posts: List<SocialPost>,
    onStoryClick: (StoryItem) -> Unit,
    onAddStoryClick: () -> Unit,
    onArtistClick: (String) -> Unit,
    onLikePost: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    onSavePost: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(HetashreeBlack)
            .testTag("home_feed_list"),
        contentPadding = PaddingValues(bottom = 72.dp)
    ) {
        // Stories Tray (Horizontal Scroll)
        item {
            StoriesSection(
                currentUser = currentUser,
                stories = stories,
                onStoryClick = onStoryClick,
                onAddStoryClick = onAddStoryClick
            )
            HorizontalDivider(color = HetashreeBorder, thickness = 0.5.dp)
        }

        // Feed Posts
        items(posts, key = { it.id }) { post ->
            PostCard(
                post = post,
                onArtistClick = { onArtistClick(post.artistIdCode) },
                onLikeClick = { onLikePost(post.id) },
                onCommentClick = { onCommentClick(post.id) },
                onSaveClick = { onSavePost(post.id) }
            )
            HorizontalDivider(color = HetashreeBorder, thickness = 0.5.dp)
        }
    }
}

@Composable
fun StoriesSection(
    currentUser: User,
    stories: List<StoryItem>,
    onStoryClick: (StoryItem) -> Unit,
    onAddStoryClick: () -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        contentPadding = PaddingValues(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Add Your Story Item
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onAddStoryClick() }
                    .testTag("add_story_item")
            ) {
                Box(
                    modifier = Modifier.size(68.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, HetashreeBorder, CircleShape)
                    ) {
                        AsyncImage(
                            model = currentUser.avatarUrl,
                            contentDescription = "Your avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Plus badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(HetashreeGoldPrimary)
                            .border(2.dp, HetashreeBlack, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add story",
                            tint = HetashreeBlack,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Your Story",
                    color = HetashreeTextPrimary,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }
        }

        // Artist Stories
        items(stories, key = { it.id }) { story ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onStoryClick(story) }
                    .testTag("story_item_${story.id}")
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(StoryRingGradient)
                        .padding(2.5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(HetashreeBlack)
                            .padding(2.dp)
                    ) {
                        AsyncImage(
                            model = story.authorAvatarUrl,
                            contentDescription = story.authorName,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = story.authorName.split(" ").firstOrNull() ?: story.authorName,
                    color = HetashreeTextPrimary,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun PostCard(
    post: SocialPost,
    onArtistClick: () -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val heartColor by animateColorAsState(
        targetValue = if (post.isLiked) HetashreeLikeRed else HetashreeTextPrimary,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "heartColor"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(HetashreeBlack)
            .testTag("post_card_${post.id}")
    ) {
        // Post Header: Profile Pic, Name, Artist ID, More
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onArtistClick() }
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.dp, HetashreeGoldPrimary.copy(alpha = 0.5f), CircleShape)
                ) {
                    AsyncImage(
                        model = post.authorAvatarUrl,
                        contentDescription = post.authorName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = post.authorName,
                            color = HetashreeTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp
                        )
                        if (post.isVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified Artist",
                                tint = HetashreeVerifiedBlue,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = post.artistIdCode,
                            color = HetashreeGoldPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.5.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• ${post.timeAgo}",
                            color = HetashreeTextMuted,
                            fontSize = 10.5.sp
                        )
                    }
                }
            }

            IconButton(
                onClick = { /* menu */ },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Post options",
                    tint = HetashreeTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Post Media (Image)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(HetashreeDarkSurface)
        ) {
            AsyncImage(
                model = post.mediaUrl,
                contentDescription = post.caption,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Brand watermark badge
            Surface(
                color = HetashreeBlack.copy(alpha = 0.65f),
                shape = RoundedCornerShape(topStart = 8.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "HETASHREE",
                        color = HetashreeGoldBright,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "STAR",
                        color = HetashreeGoldAmber,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Action Buttons Bar (Like, Comment, Share, Save)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onLikeClick,
                    modifier = Modifier.testTag("post_like_${post.id}")
                ) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = heartColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(
                    onClick = onCommentClick,
                    modifier = Modifier.testTag("post_comment_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comment",
                        tint = HetashreeTextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                IconButton(onClick = { /* share */ }) {
                    Icon(
                        imageVector = Icons.Outlined.Send,
                        contentDescription = "Share",
                        tint = HetashreeTextPrimary,
                        modifier = Modifier.size(21.dp)
                    )
                }
            }

            IconButton(
                onClick = onSaveClick,
                modifier = Modifier.testTag("post_save_${post.id}")
            ) {
                Icon(
                    imageVector = if (post.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "Save",
                    tint = if (post.isSaved) HetashreeGoldPrimary else HetashreeTextPrimary,
                    modifier = Modifier.size(23.dp)
                )
            }
        }

        // Likes Count
        Text(
            text = "${post.likesCount} likes",
            color = HetashreeTextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 1.dp)
        )

        // Caption & Artist name
        if (post.caption.isNotBlank()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "${post.authorUsername} ",
                    color = HetashreeTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = post.caption,
                    color = HetashreeTextSecondary,
                    fontSize = 13.sp
                )
            }
        }

        // Hashtags
        if (post.hashtags.isNotEmpty()) {
            Text(
                text = post.hashtags.joinToString(" "),
                color = HetashreeGoldLight,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 2.dp)
            )
        }

        // Comments Count Link
        if (post.commentsCount > 0) {
            Text(
                text = "View all ${post.commentsCount} comments",
                color = HetashreeTextMuted,
                fontSize = 12.sp,
                modifier = Modifier
                    .clickable { onCommentClick() }
                    .padding(horizontal = 14.dp, vertical = 3.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

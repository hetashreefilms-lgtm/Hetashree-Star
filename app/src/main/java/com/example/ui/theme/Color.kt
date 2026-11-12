package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// HETASHREE STAR - Premium Black and Gold Palette
val HetashreeBlack = Color(0xFF0B0B0E)
val HetashreeDarkSurface = Color(0xFF141419)
val HetashreeCard = Color(0xFF1C1C24)
val HetashreeCardElevated = Color(0xFF24242F)
val HetashreeBorder = Color(0xFF2E2E3A)

val HetashreeGoldPrimary = Color(0xFFD4AF37)
val HetashreeGoldBright = Color(0xFFF7DF94)
val HetashreeGoldLight = Color(0xFFEBD48D)
val HetashreeGoldDark = Color(0xFF99751F)
val HetashreeGoldAmber = Color(0xFFD99E26)

val HetashreeTextPrimary = Color(0xFFFFFFFF)
val HetashreeTextSecondary = Color(0xFFA5A5B2)
val HetashreeTextMuted = Color(0xFF70707E)

val HetashreeVerifiedBlue = Color(0xFF3897F0)
val HetashreeVerifiedGold = Color(0xFFFFD700)
val HetashreeLikeRed = Color(0xFFFD1D1D)
val HetashreeSuccess = Color(0xFF4CAF50)
val HetashreeWarning = Color(0xFFFF9800)
val HetashreeError = Color(0xFFE53935)

val GoldGradient = Brush.horizontalGradient(
    colors = listOf(HetashreeGoldBright, HetashreeGoldPrimary, HetashreeGoldDark)
)

val StoryRingGradient = Brush.sweepGradient(
    colors = listOf(
        Color(0xFFFBAA47),
        Color(0xFFD91A46),
        Color(0xFFA60F93),
        Color(0xFFD4AF37),
        Color(0xFFFBAA47)
    )
)

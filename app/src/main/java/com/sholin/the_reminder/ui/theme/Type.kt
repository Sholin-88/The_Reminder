package com.sholin.the_reminder.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sholin.the_reminder.R
import kotlin.math.sin


private val Nunito = FontFamily(Font(R.font.nunito))
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Bold,
        fontSize = 35.sp,

    ),
    labelSmall = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
    )

)

object ComposeTypography {
    val defaultType = Typography
    val header = defaultType.titleLarge
    val bodyLarge = defaultType.bodyLarge
    val labelSmallBold = defaultType.labelSmall.copy(fontWeight = FontWeight.Bold)
    val bodyMedium = defaultType.bodyLarge.copy(fontSize = 14.sp, fontWeight = FontWeight.Bold)
}

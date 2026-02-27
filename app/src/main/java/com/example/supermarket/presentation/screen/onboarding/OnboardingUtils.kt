package com.example.supermarket.presentation.screen.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.supermarket.R
import com.example.supermarket.presentation.ui.theme.CustomIcons

@Composable
fun DiscountBadge(
    text: String,
    badgeColor: Color,
    size: Dp = 50.dp,
    textColor: Color = Color.White,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(badgeColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = (size.value * 0.35f).sp
        )
    }
}

@Composable
fun CategoryItem(
    imageRes: Int,
    title: String,
    countText: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(36.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = countText,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    lineHeight = 13.sp
                )
            }

            Icon(
                painter = painterResource(R.drawable.ic_vector_next),
                contentDescription = "Next Arrow",
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }

        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            modifier = Modifier
                .padding(start = 8.dp)
                .size(42.dp),
            contentScale = ContentScale.Fit
        )
    }
}

data class OnboardingPage(
    val image: Int,
    val title: String,
    val description: String,
    val bgColor: Color
)

data class CategoryItemData(
    val title: String,
    val count: String,
    val imageRes: Int
)

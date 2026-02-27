package com.example.supermarket.presentation.screen.onboarding

import android.content.Context
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.edit
import androidx.navigation.NavController
import com.example.supermarket.R
import com.example.supermarket.presentation.screen.navigation.Screens
import com.example.supermarket.presentation.ui.theme.Blue5
import com.example.supermarket.presentation.ui.theme.Gray10
import com.example.supermarket.presentation.ui.theme.Green
import com.example.supermarket.presentation.ui.theme.Green12
import com.example.supermarket.presentation.ui.theme.Pale
import com.example.supermarket.presentation.ui.theme.PaleYellow
import com.example.supermarket.presentation.ui.theme.Pink
import com.example.supermarket.presentation.ui.theme.Red10
import com.example.supermarket.presentation.ui.theme.White700
import com.example.supermarket.presentation.ui.theme.Yellow15
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


val onboardingPages = listOf(
    OnboardingPage(
        image = R.drawable.product,
        title = "Бонусы за покупки",
        description = "Получайте бонусы за покупки в магазине. Накопленные баллы можно обменять на скидки и товары.",
        bgColor = Pink
    ),
    OnboardingPage(
        image = R.drawable.basket,
        title = "Скидки на товары",
        description = "Получайте большие скидки на товары, покупайте “любимые товары” по выгодной цене, участвуйте в акциях.",
        bgColor = Gray10
    ),
    OnboardingPage(
        image = 0,
        title = "Каталог товаров",
        description = "В нашем обширном каталоге более 10 категорий. Кроме того, в каждой категории имеется огромное количество различных товаров.",
        bgColor = Pale
    ),
    OnboardingPage(
        image = 0,
        title = "Интеграция с 1C",
        description = "Интеграция с 1C позволяет синхронизировать данные о товарах, ценах и запасах, улучшая точность и эффективность работы супермаркета.",
        bgColor = PaleYellow
    )
)

val categoryList = listOf(
    CategoryItemData("Готовая еда", "320 товаров", R.drawable.ic_food),
    CategoryItemData("Молочные продукты", "295 товаров", R.drawable.ic_food),
    CategoryItemData("Сыры", "22 товара", R.drawable.ic_food),
    CategoryItemData("Овощи и зелень", "313 товаров", R.drawable.ic_food),
    CategoryItemData("Хлеб и выпечка", "317 товаров", R.drawable.ic_food)
)

@Composable
fun OnboardingScreen(navController: NavController) {
    val pagerState = rememberPagerState(
        pageCount = { onboardingPages.size }
    )
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)

    val finishOnboarding = {
        sharedPreferences.edit { putBoolean("is_onboarding_completed", true) }
        navController.navigate(Screens.MainFlow.route) {
            popUpTo("onboarding_screen") { inclusive = true }
        }
    }

    val offset = pagerState.currentPageOffsetFraction
    val page = pagerState.currentPage

    val bgColor = when {
        offset > 0f && page < onboardingPages.size - 1 -> {
            lerp(onboardingPages[page].bgColor, onboardingPages[page + 1].bgColor, offset)
        }

        offset < 0f && page > 0 -> {
            lerp(onboardingPages[page].bgColor, onboardingPages[page - 1].bgColor, -offset)
        }

        else -> onboardingPages[page].bgColor
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.weight(1.2f))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(Color.White)
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = false
        ) { position ->
            val isPreview = LocalInspectionMode.current
            val pageItem = onboardingPages[position]

            var isVisible by remember { mutableStateOf(isPreview) }

            LaunchedEffect(Unit) {
                if (!isPreview && position == 0) {
                    delay(300)
                    isVisible = true
                }
            }

            val offsetX by animateDpAsState(
                targetValue = if (isVisible || position != 0) 0.dp else 300.dp,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                label = "slide_in"
            )

            val alpha by animateFloatAsState(
                targetValue = if (isVisible || position != 0) 1f else 0f,
                animationSpec = tween(durationMillis = 1000),
                label = "fade_in"
            )

            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1.2f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_vector),
                        contentDescription = "Vector Background",
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.BottomCenter),
                        contentScale = ContentScale.Crop
                    )

                    if (position == 0 || position == 1) {
                        Image(
                            painter = painterResource(id = pageItem.image),
                            contentDescription = "Onboarding Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.BottomCenter)
                                .offset(x = if (position == 0) offsetX else 0.dp)
                                .alpha(if (position == 0) alpha else 1f),
                            contentScale = ContentScale.Crop
                        )
                    }

                    if (position == 1) {
                        var activeBadgeIndex by remember { mutableIntStateOf(if (isPreview) 3 else 0) }

                        LaunchedEffect(pagerState.currentPage) {
                            if (!isPreview && pagerState.currentPage == 1) {
                                while (true) {
                                    delay(1500)
                                    activeBadgeIndex = (activeBadgeIndex + 1) % 5
                                }
                            }
                        }
                        val smoothAnimation =
                            tween<Float>(durationMillis = 800, easing = FastOutSlowInEasing)

                        val scale0 by animateFloatAsState(
                            if (activeBadgeIndex == 0) 1f else 0.5f,
                            smoothAnimation,
                            label = ""
                        )
                        val alpha0 by animateFloatAsState(
                            if (activeBadgeIndex == 0) 1f else 0f,
                            smoothAnimation,
                            label = ""
                        )
                        DiscountBadge(
                            "15%",
                            Yellow15,
                            55.dp,
                            Color.Black,
                            Modifier
                                .align(Alignment.Center)
                                .offset(80.dp, (-140).dp)
                                .scale(scale0)
                                .alpha(alpha0)
                        )

                        val scale1 by animateFloatAsState(
                            if (activeBadgeIndex == 1) 1f else 0.5f,
                            smoothAnimation,
                            label = ""
                        )
                        val alpha1 by animateFloatAsState(
                            if (activeBadgeIndex == 1) 1f else 0f,
                            smoothAnimation,
                            label = ""
                        )
                        DiscountBadge(
                            "12%",
                            Green12,
                            65.dp,
                            Color.White,
                            Modifier
                                .align(Alignment.Center)
                                .offset(0.dp, 0.dp)
                                .scale(scale1)
                                .alpha(alpha1)
                        )

                        val scale2 by animateFloatAsState(
                            if (activeBadgeIndex == 2) 1f else 0.5f,
                            smoothAnimation,
                            label = ""
                        )
                        val alpha2 by animateFloatAsState(
                            if (activeBadgeIndex == 2) 1f else 0f,
                            smoothAnimation,
                            label = ""
                        )
                        DiscountBadge(
                            "10%",
                            Red10,
                            60.dp,
                            Color.White,
                            Modifier
                                .align(Alignment.Center)
                                .offset((-80).dp, (-120).dp)
                                .scale(scale2)
                                .alpha(alpha2)
                        )

                        val scale3 by animateFloatAsState(
                            if (activeBadgeIndex == 3) 1f else 0.5f,
                            smoothAnimation,
                            label = ""
                        )
                        val alpha3 by animateFloatAsState(
                            if (activeBadgeIndex == 3) 1f else 0f,
                            smoothAnimation,
                            label = ""
                        )
                        DiscountBadge(
                            "8%",
                            Yellow15,
                            45.dp,
                            Color.Black,
                            Modifier
                                .align(Alignment.Center)
                                .offset((-90).dp, 10.dp)
                                .scale(scale3)
                                .alpha(alpha3)
                        )

                        val scale4 by animateFloatAsState(
                            if (activeBadgeIndex == 4) 1f else 0.5f,
                            smoothAnimation,
                            label = ""
                        )
                        val alpha4 by animateFloatAsState(
                            if (activeBadgeIndex == 4) 1f else 0f,
                            smoothAnimation,
                            label = ""
                        )
                        DiscountBadge(
                            "5%",
                            Blue5,
                            50.dp,
                            Color.White,
                            Modifier
                                .align(Alignment.Center)
                                .offset(90.dp, 30.dp)
                                .scale(scale4)
                                .alpha(alpha4)
                        )
                    }

                    if (position == 2) {
                        val isPreview = LocalInspectionMode.current
                        var currentIndex by remember { mutableIntStateOf(if (isPreview) 3 else 0) }

                        LaunchedEffect(pagerState.currentPage) {
                            if (!isPreview && pagerState.currentPage == 2) {
                                while (true) {
                                    delay(1500)
                                    currentIndex++
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 80.dp, start = 24.dp, end = 24.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            val startIdx = maxOf(0, currentIndex - 4)
                            val endIdx = currentIndex + 1

                            for (i in startIdx..endIdx) {
                                key(i) {
                                    val item = categoryList[i % categoryList.size]
                                    val pos = currentIndex - i
                                    val targetY = (pos * 65).dp

                                    val targetAlpha = when {
                                        pos < 0 -> 0f
                                        pos in 0..2 -> 1f
                                        pos == 3 -> 0.3f
                                        else -> 0f
                                    }

                                    val animSpecDp = tween<Dp>(
                                        durationMillis = 600,
                                        easing = FastOutSlowInEasing
                                    )
                                    val animSpecFloat = tween<Float>(
                                        durationMillis = 600,
                                        easing = FastOutSlowInEasing
                                    )

                                    val yOffset by animateDpAsState(
                                        targetValue = targetY,
                                        animationSpec = animSpecDp,
                                        label = ""
                                    )
                                    val itemAlpha by animateFloatAsState(
                                        targetValue = targetAlpha,
                                        animationSpec = animSpecFloat,
                                        label = ""
                                    )

                                    CategoryItem(
                                        imageRes = item.imageRes,
                                        title = item.title,
                                        countText = item.count,
                                        modifier = Modifier
                                            .offset(y = yOffset)
                                            .alpha(itemAlpha)
                                            .zIndex(i.toFloat())
                                    )
                                }
                            }
                        }
                    }
                    if (position == 3) {
                        val isPreview = LocalInspectionMode.current

                        var step by remember { mutableIntStateOf(if (isPreview) 3 else 0) }

                        LaunchedEffect(pagerState.currentPage) {
                            if (!isPreview) {
                                if (pagerState.currentPage == 3) {
                                    step = 0
                                    delay(100)
                                    step = 1
                                    delay(300)
                                    step = 2
                                    delay(400)
                                    step = 3
                                } else {
                                    step = 0
                                }
                            }
                        }

                        val animSpecX =
                            tween<Dp>(durationMillis = 800, easing = FastOutSlowInEasing)
                        val animSpecAlpha =
                            tween<Float>(durationMillis = 800, easing = FastOutSlowInEasing)

                        val m1X by animateDpAsState(
                            targetValue = if (step >= 1) (-35).dp else 400.dp,
                            animationSpec = animSpecX,
                            label = ""
                        )
                        val m1Alpha by animateFloatAsState(
                            targetValue = if (step >= 1) 1f else 0f,
                            animationSpec = animSpecAlpha,
                            label = ""
                        )

                        val m2X by animateDpAsState(
                            targetValue = if (step >= 2) (95).dp else 400.dp,
                            animationSpec = animSpecX,
                            label = ""
                        )
                        val m2Alpha by animateFloatAsState(
                            targetValue = if (step >= 2) 1f else 0f,
                            animationSpec = animSpecAlpha,
                            label = ""
                        )

                        val logoX by animateDpAsState(
                            targetValue = if (step >= 3) 100.dp else 400.dp,
                            animationSpec = animSpecX,
                            label = ""
                        )
                        val logoAlpha by animateFloatAsState(
                            targetValue = if (step >= 3) 1f else 0f,
                            animationSpec = animSpecAlpha,
                            label = ""
                        )

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.mockup2),
                                contentDescription = "Молочные продукты",
                                modifier = Modifier
                                    .offset(x = m1X, y = 40.dp)
                                    .rotate(-10f)
                                    .width(200.dp)
                                    .aspectRatio(245f / 400f)
                                    .alpha(m1Alpha)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Image(
                                painter = painterResource(id = R.drawable.mockup1),
                                contentDescription = "Готовая еда",
                                modifier = Modifier
                                    .offset(x = m2X, y = 100.dp)
                                    .width(200.dp)
                                    .aspectRatio(243.45f / 450f)
                                    .alpha(m2Alpha)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Image(
                                painter = painterResource(id = R.drawable.logo_1c),
                                contentDescription = "1C Logo",
                                modifier = Modifier
                                    .offset(x = logoX, y = (-300).dp)
                                    .size(90.dp)
                                    .alpha(logoAlpha)
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(Color.White)
                        .padding(horizontal = 32.dp)
                ) {
                    Spacer(modifier = Modifier.height(48.dp))

                    Text(
                        text = pageItem.title,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = pageItem.description,
                        fontSize = 16.sp,
                        color = Gray,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(1.2f))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(onboardingPages.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        val width = animateDpAsState(
                            targetValue = if (isSelected) 24.dp else 8.dp,
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                            label = "indicator"
                        )

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .height(8.dp)
                                .width(width.value)
                                .clip(CircleShape)
                                .background(if (isSelected) Green else Gray10)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (pagerState.currentPage < onboardingPages.size - 1) {
                        TextButton(
                            onClick = { finishOnboarding() }
                        ) {
                            Text(
                                text = "Пропустить",
                                color = White700,
                                fontSize = 16.sp
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    Button(
                        onClick = {
                            if (pagerState.currentPage == onboardingPages.size - 1) {
                                finishOnboarding()
                            } else {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Green),
                        modifier = Modifier.size(65.dp),
                        contentPadding = PaddingValues(0.dp),
                        elevation = ButtonDefaults.buttonElevation(5.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (pagerState.currentPage == onboardingPages.size - 1) {
                                    R.drawable.ic_done
                                } else {
                                    R.drawable.ic_next
                                }
                            ),
                            contentDescription = "Next",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}


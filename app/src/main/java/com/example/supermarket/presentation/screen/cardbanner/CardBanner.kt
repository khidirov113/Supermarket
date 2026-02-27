package com.example.supermarket.presentation.screen.cardbanner

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil3.compose.AsyncImage
import com.example.supermarket.R
import com.example.supermarket.domain.entity.Banner
import com.example.supermarket.domain.entity.Product
import com.example.supermarket.presentation.ui.theme.Black
import com.example.supermarket.presentation.ui.theme.Green
import com.example.supermarket.presentation.utils.CardBannerShimmer
import kotlin.math.absoluteValue


@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    onClickAdd: () -> Unit,
    onNotification: () -> Unit,
    onHistoryClick: () -> Unit,
    balance: String?,
    isAuthenticated: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(modifier = Modifier.weight(1f)) {
            if (isAuthenticated) {
                if (balance != null) {
                    Text(
                        text = balance,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Green,
                        letterSpacing = 0.5.sp
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onClickAdd() }
                ) {
                    Text(
                        text = stringResource(id = R.string.create_system),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Green
                    )

                    Spacer(modifier = Modifier.width(3.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.ic_vector_system),
                        contentDescription = null,
                        tint = Green,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_notification),
                contentDescription = stringResource(R.string.notification),
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .clickable { onNotification() },
                tint = Color.Black
            )

            if (isAuthenticated) {
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_bonus),
                    contentDescription = "History",
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .clickable { onHistoryClick() },
                    tint = Color.Black
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardBanner(
    banners: List<Banner>,
    onClickBanner: (Long) -> Unit
) {
    if (banners.isEmpty()) {
        CardBannerShimmer()
    } else {
        val pagerState = rememberPagerState(pageCount = { banners.size })
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                HorizontalPager(
                    state = pagerState,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    pageSpacing = 12.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 10.dp)
                ) { page ->

                    val currentBanner = banners[page]

                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .fillMaxSize()
                            .graphicsLayer {
                                val pageOffset = (
                                        (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                                        ).absoluteValue

                                val scale = lerp(
                                    start = 0.85f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )
                                scaleX = scale
                                scaleY = scale

                                alpha = lerp(
                                    start = 0.5f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )
                            }
                            .clickable { onClickBanner(currentBanner.id) }
                    ) {
                        AsyncImage(
                            model = currentBanner.image,
                            contentDescription = "Banner Image ${currentBanner.id}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            placeholder = ColorPainter(Color.LightGray),
                            error = ColorPainter(Color.Gray)
                        )
                    }
                }
            }
            if (banners.size > 1) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val isSelected = pagerState.currentPage == iteration
                        val color = if (isSelected) Green else Color.Gray.copy(alpha = 0.5f)
                        val width = if (isSelected) 16.dp else 8.dp
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .height(6.dp)
                                .width(width)
                                .animateContentSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DiscountsWeek(
    modifier: Modifier = Modifier,
    onCLickAll: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 8.dp, end = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.Discounts_of_the_week),
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                fontSize = 16.sp
            )
            TextButton(
                onClick = { onCLickAll() },
            )
            {
                Text(
                    text = stringResource(R.string.all),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Green
                )
            }
        }
    }
}


@Composable
fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier,
    onClickProduct: (Long) -> Unit
) {
    Surface(
        modifier = modifier
            .padding(4.dp)
            .width(110.dp)
            .clickable { onClickProduct(product.id) },
        shadowElevation = 4.dp,
        color = Color.White,
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                AsyncImage(
                    model = product.image,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(R.drawable.ic_foot),
                    error = painterResource(R.drawable.ic_foot),
                    fallback = painterResource(R.drawable.ic_foot),
                    modifier = Modifier.fillMaxSize(),
                )
            }
            Surface(
                color = Green,
                modifier = Modifier
                    .height(20.dp)
                    .wrapContentWidth(),
                shape = RoundedCornerShape(topEnd = 16.dp, bottomStart = 16.dp),
            ) {
                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = if (product.inStock) "В наличии" else "Нет в наличии",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Medium,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                Text(
                    text = "${product.title} ${product.description}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black,
                    maxLines = 2,
                    lineHeight = 12.sp,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    if (product.salePrice < product.price) {
                        Text(
                            text = "${product.salePrice} TJS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "${product.price} TJS",
                            fontSize = 12.sp,
                            color = Color.LightGray,
                            style = TextStyle(textDecoration = TextDecoration.LineThrough)
                        )
                    } else {
                        Text(
                            text = "${product.price} TJS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}


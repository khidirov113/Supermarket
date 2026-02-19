package com.example.supermarket.presentation.screen.home.product

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.supermarket.R
import com.example.supermarket.presentation.screen.home.utils.ProductCard
import com.example.supermarket.presentation.ui.theme.Green
import com.example.supermarket.presentation.ui.theme.Grey200
import com.example.supermarket.presentation.utils.ProductDetailSkeleton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailSheet(
    productId: Long,
    onDismiss: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val product by viewModel.selectedProduct.collectAsState()
    val similarProducts by viewModel.similarProducts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val scrollState = rememberScrollState()

    LaunchedEffect(productId) {
        viewModel.loadProductDetails(productId)
        scrollState.scrollTo(0)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White,
        dragHandle = null
    ) {
        if (isLoading) {
            ProductDetailSkeleton()
        } else {
            product?.let { currentProduct ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(bottom = 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .background(Grey200, CircleShape)
                                .size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    val images = currentProduct.images.ifEmpty { listOf("") }
                    val pagerState = rememberPagerState(pageCount = { images.size })

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(horizontal = 20.dp)
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp))
                        ) { page ->
                            AsyncImage(
                                model = images[page],
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit,
                                placeholder = painterResource(R.drawable.ic_foot),
                                error = painterResource(R.drawable.ic_foot),
                            )
                        }

                        if (currentProduct.salePercent > 0) {
                            Surface(
                                color = Green,
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .height(30.dp),
                                shape = RoundedCornerShape(topEnd = 16.dp, bottomStart = 16.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                ) {
                                    Text(
                                        text = "-${currentProduct.salePercent.toInt()}%",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    if (images.isNotEmpty()) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(images.size) { iteration ->
                                val isSelected = pagerState.currentPage == iteration
                                Box(
                                    modifier = Modifier
                                        .padding(3.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) Green else Color.LightGray)
                                        .height(6.dp)
                                        .width(if (isSelected) 16.dp else 6.dp)
                                        .animateContentSize()
                                )
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text(
                            text = if (currentProduct.inStock) "В наличии" else "Нет в наличии",
                            color = Green,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${currentProduct.title} ${currentProduct.description ?: ""}",
                            fontSize = 16.sp,
                            color = Color.Black,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Похожие товары",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 5.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(similarProducts) { item ->
                            ProductCard(
                                product = item,
                                modifier = Modifier.width(130.dp),
                                onClickProduct = { id ->
                                    viewModel.loadProductDetails(id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
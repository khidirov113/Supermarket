package com.example.supermarket.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.supermarket.presentation.screen.home.banner.BannerViewModel
import com.example.supermarket.presentation.screen.home.product.ProductDetailSheet
import com.example.supermarket.presentation.screen.home.product.ProductVewModel
import com.example.supermarket.presentation.screen.home.utils.AppTopBar
import com.example.supermarket.presentation.screen.home.utils.CardBanner
import com.example.supermarket.presentation.screen.home.utils.DiscountsWeek
import com.example.supermarket.presentation.screen.home.utils.ProductCard
import com.example.supermarket.presentation.ui.theme.Green
import com.example.supermarket.presentation.ui.theme.White200
import com.example.supermarket.presentation.utils.AuthBottomSheet
import com.example.supermarket.presentation.utils.ProductCardShimmer
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onBannerClick: (Long) -> Unit,
    onClickWeekSale: () -> Unit,
    onNavigateToAuth: () -> Unit,
    bannerViewModel: BannerViewModel = hiltViewModel(),
    productViewModel: ProductVewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val banners by bannerViewModel.banners.collectAsState()
    val products by productViewModel.products.collectAsState()
    val isAuthenticated by homeViewModel.isAuthenticated.collectAsState()

    var showBottomSheetById by remember { mutableStateOf<Long?>(null) }
    var showAuthSheet by remember { mutableStateOf(false) }
    val balance by homeViewModel.userBalance

    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()

    val state = rememberPullToRefreshState()

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = modifier.fillMaxSize()) {
            AppTopBar(
                onClickAdd = {
                    if (!isAuthenticated) {
                        showAuthSheet = true
                    }
                },
                balance = if (isAuthenticated) balance else null,
                onNotification = {}
            )

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 0.8.dp,
                color = White200
            )

            Box(modifier.fillMaxWidth()) {
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    state = state,
                    onRefresh = {
                        scope.launch {
                            isRefreshing = true

                            val bannersJob = launch { bannerViewModel.fetchBanners() }
                            val productsJob = launch { productViewModel.fetchProducts() }
                            val balanceJob =
                                launch { if (isAuthenticated) homeViewModel.fetchBalance() }

                            delay(2000)

                            joinAll(bannersJob, productsJob, balanceJob)

                            isRefreshing = false
                        }
                    },
                    indicator = {
                        PullToRefreshDefaults.Indicator(
                            state = state,
                            isRefreshing = isRefreshing,
                            modifier = Modifier.align(Alignment.TopCenter),
                            containerColor = Color.White,
                            color = Green
                        )
                    }
                ) {

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item(span = { GridItemSpan(2) }) {
                            CardBanner(
                                banners = banners,
                                onClickBanner = onBannerClick
                            )
                        }

                        item(span = { GridItemSpan(2) }) {
                            DiscountsWeek(
                                modifier = Modifier.fillMaxWidth(),
                                onCLickAll = { onClickWeekSale() }
                            )
                        }
                        item(span = { GridItemSpan(2) }) {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp)
                            ) {
                                if (products.isEmpty()) {
                                    items(10) {
                                        ProductCardShimmer()
                                    }
                                } else {
                                    items(products) { product ->
                                        ProductCard(
                                            product = product,
                                            onClickProduct = { id -> showBottomSheetById = id }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                if (showAuthSheet) {
                    AuthBottomSheet(
                        onDismiss = { showAuthSheet = false },
                        onLoginClick = {
                            showAuthSheet = false
                            onNavigateToAuth()
                        }
                    )
                }

                showBottomSheetById?.let { id ->
                    ProductDetailSheet(
                        productId = id,
                        onDismiss = { showBottomSheetById = null }
                    )
                }
            }
        }
    }
}

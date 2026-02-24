package com.example.supermarket.presentation.screen.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.supermarket.presentation.screen.cardbanner.BannerViewModel
import com.example.supermarket.presentation.screen.cardbanner.AppTopBar
import com.example.supermarket.presentation.screen.cardbanner.CardBanner
import com.example.supermarket.presentation.screen.cardbanner.DiscountsWeek
import com.example.supermarket.presentation.screen.cardbanner.ProductCard
import com.example.supermarket.presentation.screen.product.ProductViewModel
import com.example.supermarket.presentation.screen.productdelail.ProductDetailSheet
import com.example.supermarket.presentation.ui.theme.Green
import com.example.supermarket.presentation.ui.theme.White200
import com.example.supermarket.presentation.utils.AuthBottomSheet
import com.example.supermarket.presentation.utils.ProductCardShimmer
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onBannerClick: (Long) -> Unit,
    onClickWeekSale: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onNotificationClick: () -> Unit,
    bannerViewModel: BannerViewModel = hiltViewModel(),
    productViewModel: ProductViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val banners by bannerViewModel.banners.collectAsState()
    val products by productViewModel.products.collectAsState()
    val isAuthenticated by homeViewModel.isAuthenticated.collectAsState()

    var showAuthSheet by remember { mutableStateOf(false) }
    val balance by homeViewModel.userBalance

    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()
    val errorMessage by productViewModel.errorMassage.collectAsState()
    val errorMessageBanner by bannerViewModel.errorMassage.collectAsState()


    var showBottomSheetById by remember { mutableStateOf<Long?>(null) }

    val context = LocalContext.current

    LaunchedEffect(errorMessage, errorMessageBanner) {
        errorMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            productViewModel.cleanError()
        }
        errorMessageBanner?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            bannerViewModel.cleanError()
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppTopBar(
                    onClickAdd = {
                        if (!isAuthenticated) {
                            showAuthSheet = true
                        }
                    },
                    balance = if (isAuthenticated) balance else null,
                    onNotification = onNotificationClick
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 0.8.dp,
                color = White200
            )

            PullToRefreshBox(
                isRefreshing = isRefreshing,
                state = pullToRefreshState,
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
                        state = pullToRefreshState,
                        isRefreshing = isRefreshing,
                        modifier = Modifier.align(Alignment.TopCenter),
                        containerColor = Color.White,
                        color = Green
                    )
                },
                modifier = Modifier.fillMaxSize()
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item(span = { GridItemSpan(2) }) {
                        CardBanner(
                            banners = banners,
                            onClickBanner = onBannerClick
                        )
                    }

                    item(span = { GridItemSpan(2) }) {
                        DiscountsWeek(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = (-15).dp),
                            onCLickAll = { onClickWeekSale() }
                        )
                    }

                    item(span = { GridItemSpan(2) }) {
                        LazyRow(
                            modifier = Modifier.offset(y = (-15).dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 20.dp)
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
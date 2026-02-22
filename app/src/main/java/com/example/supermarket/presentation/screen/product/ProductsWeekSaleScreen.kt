package com.example.supermarket.presentation.screen.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.supermarket.R
import com.example.supermarket.presentation.screen.cardbanner.ProductCard
import com.example.supermarket.presentation.screen.productdelail.ProductDetailSheet
import com.example.supermarket.presentation.utils.ProductCardShimmer


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsWeekSaleScreen(
    modifier: Modifier = Modifier,
    onProductClick: (Long) -> Unit,
    onBack: () -> Unit,
    viewModel: ProductVewModel = hiltViewModel()
) {

    val products by viewModel.products.collectAsState()
    val isLoading = products.isEmpty()
    var showBottomSheetById by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        modifier = modifier
            .background(Color.White)
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Скидки недели") },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable {
                                onBack()
                            },
                        painter = painterResource(id = R.drawable.vector__stroke_),
                        contentDescription = "Back",
                        tint = Color.Unspecified
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (isLoading) {
                    items(6) {
                        ProductCardShimmer()
                    }
                } else {
                    items(products) { singleProduct ->
                        ProductCard(
                            product = singleProduct,
                            onClickProduct = { id ->
                                showBottomSheetById = id
                            }
                        )
                    }
                }
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
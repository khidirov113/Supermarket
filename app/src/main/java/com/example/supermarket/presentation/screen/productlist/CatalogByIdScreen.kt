package com.example.supermarket.presentation.screen.productlist

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.supermarket.R
import com.example.supermarket.domain.entity.Product
import com.example.supermarket.domain.entity.SubCategory
import com.example.supermarket.presentation.screen.cardbanner.ProductCard
import com.example.supermarket.presentation.screen.catalog.CatalogViewModel
import com.example.supermarket.presentation.screen.productdelail.ProductDetailSheet
import com.example.supermarket.presentation.ui.theme.Green
import com.example.supermarket.presentation.ui.theme.Grey200

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogByIdScreen(
    catalogId: Long,
    onBack: () -> Unit,
    viewModel: CatalogViewModel = hiltViewModel()
) {
    val categoryState by viewModel.categoryByIdState.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    var seeAllModeSubCategory by remember { mutableStateOf<SubCategory?>(null) }

    var showBottomSheetById by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(catalogId) {
        viewModel.loadProductsBySubCategory(catalogId)
    }

    val topBarTitle = if (seeAllModeSubCategory != null) {
        seeAllModeSubCategory?.title ?: ""
    } else {
        categoryState?.title ?: ""
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = topBarTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (seeAllModeSubCategory != null) {
                            seeAllModeSubCategory = null
                        } else {
                            onBack()
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.vector__stroke_),
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        if (categoryState == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Green)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White)
            ) {
                val subCategories = categoryState?.subcategories ?: emptyList()

                if (seeAllModeSubCategory == null) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            CustomTabPill(
                                text = "Все",
                                isSelected = selectedTabIndex == 0,
                                onClick = { selectedTabIndex = 0 }
                            )
                        }
                        itemsIndexed(subCategories) { index, subCat ->
                            CustomTabPill(
                                text = subCat.title,
                                isSelected = selectedTabIndex == index + 1,
                                onClick = { selectedTabIndex = index + 1 }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (seeAllModeSubCategory != null) {
                    ProductsGridList(
                        products = seeAllModeSubCategory?.products ?: emptyList(),
                        onProductClick = { showBottomSheetById = it }
                    )
                } else if (selectedTabIndex == 0) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(subCategories) { subCat ->
                            SubCategorySectionBlock(
                                subCategory = subCat,
                                onSeeAllClick = { seeAllModeSubCategory = subCat },
                                onProductClick = { showBottomSheetById = it }
                            )
                        }
                    }
                } else {
                    val currentSubCat = subCategories.getOrNull(selectedTabIndex - 1)
                    ProductsGridList(
                        products = currentSubCat?.products ?: emptyList(),
                        onProductClick = { showBottomSheetById = it }
                    )
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

@Composable
fun CustomTabPill(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) Green else Grey200)
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
@Composable
fun SubCategorySectionBlock(
    subCategory: SubCategory,
    onSeeAllClick: () -> Unit,
    onProductClick: (Long) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = subCategory.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Text(
                text = "Все",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Green,
                modifier = Modifier.clickable { onSeeAllClick() }
            )
        }

        val products = subCategory.products ?: emptyList()

        if (products.isEmpty()) {
            Text(
                text = "Продукт пока нет",
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.Gray
            )
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        onClickProduct = { id -> onProductClick(id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductsGridList(
    products: List<Product>,
    onProductClick: (Long) -> Unit
) {
    if (products.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_no_product),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = Color.LightGray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Продукт пока нет",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(products) { product ->
                ProductCard(
                    product = product,
                    onClickProduct = { id -> onProductClick(id) }
                )
            }
        }
    }
}
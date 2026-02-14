package com.example.supermarket.presentation.screen.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.supermarket.presentation.screen.home.product.ProductDetailSheet
import com.example.supermarket.presentation.screen.home.utils.ProductCard
import com.example.supermarket.presentation.ui.theme.Black
import com.example.supermarket.presentation.ui.theme.Green
import com.example.supermarket.presentation.ui.theme.Grey
import com.example.supermarket.presentation.ui.theme.Grey200
import com.example.supermarket.presentation.ui.theme.White

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onProductClick: (Long) -> Unit,
) {
    val query by viewModel.searchQuery
    val results by viewModel.searchResult
    val isLoading by viewModel.isLoading

    var showBottomSheetById by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        containerColor = Grey200,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarSearch(
                query = query,
                onQueryChange = { viewModel.onQueryChange(it) },
                onBack = onBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(White)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Green,
                        strokeWidth = 3.dp
                    )
                }
            }
            else if (results.isNotEmpty()) {
                Text(
                    text = "Найден ${results.size} товар",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(results) { product ->
                        ProductCard(
                            product = product,
                            onClickProduct = { id -> showBottomSheetById = id }
                        )
                    }
                }
            } else if (query.isNotEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "По данному запросу ничего не найдено",
                        textAlign = TextAlign.Center,
                        color = Color.Gray
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

@Composable
fun TopBarSearch(
    query: String,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit
) {
    Surface(
        color = Grey200,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 34.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Поиск", color = Black) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(25.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = White,
                    unfocusedContainerColor = White,
                    disabledContainerColor = White,
                    cursorColor = Green,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Text(
                text = "Отменить",
                modifier = Modifier
                    .padding(start = 12.dp)
                    .clickable { onBack() },
                color = Green,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }
    }
}
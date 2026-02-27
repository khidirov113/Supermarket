package com.example.supermarket.presentation.screen.banner

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.supermarket.R
import com.example.supermarket.presentation.utils.shimmerEffect


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BannerDetailScreen(
    bannerId: Long,
    onBack: () -> Unit,
    viewModel: BannerDetailViewModel = hiltViewModel()
) {

    val banner by viewModel.banner.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMassage.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(errorMessage) {
        errorMessage?.let { msg ->
            Toast.makeText(context,msg, Toast.LENGTH_LONG).show()
            viewModel.cleanError()
        }
    }

    LaunchedEffect(bannerId) {
        viewModel.loadBannerFromFlow(bannerId)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        if (banner != null) {
            val item = banner!!
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Box {
                        AsyncImage(
                            model = item.image,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(230.dp),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = {
                                onBack()
                            },
                            modifier = Modifier
                                .padding(top = 16.dp, start = 8.dp)
                                .statusBarsPadding()
                                .align(Alignment.TopStart)
                                .background(
                                    Color.Transparent,
                                    CircleShape
                                )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_vector_stroke),
                                contentDescription = "Back",
                                tint = Color.Unspecified
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = item.title,
                            fontSize = 16.sp,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = item.description,
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black
                        )
                    }
                }
            }
        }else {
            BannerShimmerSkeleton()
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = 16.dp, start = 8.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_vector_stroke),
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }
    }
}


@Composable
fun BannerShimmerSkeleton() {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(0.6f)
                .height(24.dp)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(12.dp))
        repeat(3) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxWidth()
                    .height(16.dp)
                    .shimmerEffect()
            )
        }
    }
}
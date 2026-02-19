package com.example.supermarket.presentation.screen.notification

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.example.supermarket.R
import com.example.supermarket.domain.entity.Notification
import com.example.supermarket.presentation.screen.home.HomeViewModel
import com.example.supermarket.presentation.ui.theme.Green
import com.example.supermarket.presentation.ui.theme.White400
import com.example.supermarket.presentation.ui.theme.White500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val isAuthenticated by homeViewModel.isAuthenticated.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text(text = "Новости и уведомления", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = White400,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(12.dp)),
                indicator = {},
                divider = {}
            ) {
                val tabs = listOf("Уведомление", "Новости")
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Tab(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color.White else Color.Transparent),
                        text = {
                            Text(
                                text = title,
                                color = if (isSelected) Color.Black else Color.Gray,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            if (!isAuthenticated) {
                EmptyNotificationPlaceholder(isNews = selectedTab == 1)
            } else {
                val currentList = if (selectedTab == 0) state.notifications else state.news
                val isNewsTab = selectedTab == 1

                NotificationListContent(
                    isLoading = state.isLoading,
                    items = currentList,
                    isNews = isNewsTab
                )
            }
        }
    }
}

@Composable
fun NotificationListContent(
    isLoading: Boolean,
    items: List<Notification>,
    isNews: Boolean
) {
    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Green)
        }
    } else if (items.isEmpty()) {
        EmptyNotificationPlaceholder(isNews = isNews)
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            if (isNews) {
                items(items) { item ->
                    NotificationItem(notification = item, isNews = true)
                }
            } else {
                val grouped = items.groupBy { it.createdAt.split("T")[0] }
                grouped.forEach { (date, groupedItems) ->
                    item {
                        Text(
                            text = date,
                            modifier = Modifier.padding(vertical = 8.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                    }
                    items(groupedItems) { item ->
                        NotificationItem(notification = item, isNews = false)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: Notification, isNews: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White500),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            if (isNews) {
                if (notification.iconPath.isNotEmpty()) {
                    SubcomposeAsyncImage(
                        model = notification.iconPath,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.LightGray.copy(alpha = 0.3f)),
                        contentScale = ContentScale.Crop,
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = Green,
                                    modifier = Modifier.size(30.dp),
                                    strokeWidth = 3.dp
                                )
                            }
                        }
                    )
                    Spacer(Modifier.height(12.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    val formattedDate = try {
                        notification.createdAt.split("T")[0].split("-").reversed().joinToString(".")
                    } catch (e: Exception) {
                        notification.createdAt
                    }

                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Green,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Green.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_sale_percent),
                            contentDescription = null,
                            tint = Green,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = notification.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = notification.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyNotificationPlaceholder(isNews: Boolean) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_notification_off),
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = Color.LightGray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (isNews) "Новостей пока нет" else "Уведомлений пока нет",
                color = Color.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
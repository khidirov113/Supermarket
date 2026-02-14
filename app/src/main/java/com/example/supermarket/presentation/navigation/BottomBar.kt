package com.example.supermarket.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.supermarket.R
import com.example.supermarket.presentation.screen.home.HomeViewModel
import com.example.supermarket.presentation.screen.home.utils.QrCodeBottomSheet
import com.example.supermarket.presentation.ui.theme.Green
import com.example.supermarket.presentation.ui.theme.Grey
import com.example.supermarket.presentation.ui.theme.Grey100
import com.example.supermarket.presentation.utils.AuthBottomSheet


@Composable
fun BottomBar(
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isAuthenticated by homeViewModel.isAuthenticated.collectAsState()
    var showQrSheet by remember { mutableStateOf(false) }
    var showAuthSheet by remember { mutableStateOf(false) }

    val isSearch = currentRoute?.startsWith("search") == true
    val isBannerDetail = currentRoute?.startsWith("banner_detail") == true
    val isProductsWeekSale = currentRoute?.startsWith("product_week_sale") == true
    val isAuthScreen = currentRoute?.startsWith("auth_screen") == true
    val isProfile = currentRoute?.startsWith("profile") == true
    val showBottomBar = !isBannerDetail && !isSearch && !isProductsWeekSale && !isAuthScreen && !isProfile



    Scaffold(
        floatingActionButton = {
            if (showBottomBar) {
                FloatingActionButton(
                    onClick = {
                        if (isAuthenticated) {
                            // 2-ҲОЛАТ: Фойдаланувчи кирган бўлса, QR-код ойнасини очамиз
                            showQrSheet = true
                        } else {
                            // 1-ҲОЛАТ: Фойдаланувчи меҳмон бўлса, авторизация таклиф қиламиз
                            showAuthSheet = true
                        }
                    },
                    containerColor = Green,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(
                        0.dp,
                        0.dp
                    ),
                    modifier = Modifier
                        .offset(y = 80.dp),
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_qr),
                        contentDescription = null,
                        Modifier.size(24.dp),
                        tint = Color.White
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onItemSelected = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (showBottomBar) padding else PaddingValues(0.dp))
        ) {
            NavGraph(navController = navController)
        }
        if (showAuthSheet) {
            AuthBottomSheet(
                onDismiss = { showAuthSheet = false },
                onLoginClick = {
                    showAuthSheet = false
                    // AuthScreen га йўналтириш
                    navController.navigate(Screens.AuthScreen.route)
                }
            )
        }

        if (showQrSheet) {
            QrCodeBottomSheet(
                onDismiss = { showQrSheet = false }
            )
        }
    }
}

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onItemSelected: (String) -> Unit
) {

    val selectedColor = Green
    val unselectedColor = Grey100

    NavigationBar(
        containerColor = Grey
    ) {
        val menuItems = listOf(
            Triple("Главная", R.drawable.ic_home1, Screens.Home.route),
            Triple("Каталог", R.drawable.ic_text_search, "catalog")
        )
        val menuItemsEnd = listOf(
            Triple("Карта", R.drawable.ic_map, "map"),
            Triple("Еще", R.drawable.ic_settings_, "settings")
        )

        val itemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = selectedColor,
            selectedTextColor = selectedColor,
            unselectedIconColor = unselectedColor,
            unselectedTextColor = unselectedColor,
            indicatorColor = Color.Transparent
        )

        menuItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.third,
                onClick = { onItemSelected(item.third) },
                label = { Text(item.first) },
                colors = itemColors,
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(item.second),
                        contentDescription = null
                    )
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        menuItemsEnd.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.third,
                onClick = { onItemSelected(item.third) },
                label = { Text(item.first) },
                colors = itemColors,
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(item.second),
                        contentDescription = null
                    )
                }
            )
        }
    }
}

@Composable
fun SimpleTextScreen(
    text: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = text)
    }
}




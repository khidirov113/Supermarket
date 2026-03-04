package com.example.supermarket.presentation.screen.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.supermarket.R
import com.example.supermarket.presentation.screen.catalog.CatalogScreen
import com.example.supermarket.presentation.screen.home.HomeScreen
import com.example.supermarket.presentation.screen.home.HomeViewModel
import com.example.supermarket.presentation.screen.map.MapScreen
import com.example.supermarket.presentation.screen.qrcode.QrCodeBottomSheet
import com.example.supermarket.presentation.screen.settings.SettingScreen
import com.example.supermarket.presentation.ui.theme.Green
import com.example.supermarket.presentation.ui.theme.Grey
import com.example.supermarket.presentation.ui.theme.Grey100
import com.example.supermarket.presentation.utils.AuthBottomSheet

@Composable
fun MainScreen(
    rootNavController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isAuthenticated by homeViewModel.isAuthenticated.collectAsState()
    var showQrSheet by remember { mutableStateOf(false) }
    var showAuthSheet by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isAuthenticated == true) showQrSheet = true else showAuthSheet = true
                },
                containerColor = Green,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 5.dp,
                    pressedElevation = 8.dp
                ),
                modifier = Modifier.offset(y = 80.dp),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_qr),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = { route ->
                    bottomNavController.navigate(route) {
                        popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            NavHost(
                navController = bottomNavController,
                startDestination = Screens.Home.route
            ) {
                composable(Screens.Home.route) {
                    HomeScreen(
                        onBannerClick = { id ->
                            rootNavController.navigate(
                                Screens.BannerDetail.route.replace(
                                    "{bannerId}",
                                    id.toString()
                                )
                            )
                        },
                        onClickWeekSale = { rootNavController.navigate(Screens.ProductWeekSale.route) },
                        onNavigateToAuth = { rootNavController.navigate(Screens.AuthScreen.route) },
                        onNotificationClick = { rootNavController.navigate(Screens.Notification.route) },
                        onHistoryClick = { rootNavController.navigate(Screens.TransactionHistory.route) }
                    )
                }

                composable(Screens.Catalog.route) {
                    CatalogScreen(
                        onCategoryClick = { id, _ -> rootNavController.navigate("${Screens.CatalogById.route}/$id") },
                        onSearch = { rootNavController.navigate(Screens.Search.route) },
                        onBack = { rootNavController.popBackStack() }
                    )
                }

                composable(Screens.Map.route) {
                    MapScreen()
                }

                composable(Screens.Settings.route) {
                    SettingScreen(
                        onNavigateToEditProfile = { rootNavController.navigate(Screens.Profile.route) },
                        onLogoutSuccess = {},
                        onNavigateToAuth = { rootNavController.navigate(Screens.AuthScreen.route) },
                        onPrivacyPolicy = { rootNavController.navigate(Screens.PrivacyPolicy.route) },
                        onNavigateToAppSettings = { rootNavController.navigate(Screens.PushNotification.route) },
                        onAboutClick = { rootNavController.navigate(Screens.About.route) }
                    )
                }
            }
        }

        if (showAuthSheet) {
            AuthBottomSheet(
                onDismiss = { showAuthSheet = false },
                onLoginClick = {
                    showAuthSheet = false
                    rootNavController.navigate(Screens.AuthScreen.route)
                }
            )
        }

        if (showQrSheet) {
            QrCodeBottomSheet(onDismiss = { showQrSheet = false })
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

    NavigationBar(containerColor = Grey) {
        val menuItems = listOf(
            Triple("Главная", R.drawable.ic_home1, Screens.Home.route),
            Triple("Каталог", R.drawable.ic_text_search, Screens.Catalog.route)
        )
        val menuItemsEnd = listOf(
            Triple("Карта", R.drawable.ic_map, Screens.Map.route),
            Triple("Еще", R.drawable.ic_settings_, Screens.Settings.route)
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
                label = { Text(item.first) }, colors = itemColors,
                icon = { Icon(ImageVector.vectorResource(item.second), null) }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        menuItemsEnd.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.third,
                onClick = { onItemSelected(item.third) },
                label = { Text(item.first) }, colors = itemColors,
                icon = { Icon(ImageVector.vectorResource(item.second), null) }
            )
        }
    }
}
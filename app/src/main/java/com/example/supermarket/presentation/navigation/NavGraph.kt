package com.example.supermarket.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.supermarket.presentation.screen.catalog.CatalogByIdScreen
import com.example.supermarket.presentation.screen.catalog.CatalogScreen
import com.example.supermarket.presentation.screen.catalog.SearchScreen
import com.example.supermarket.presentation.screen.home.HomeScreen
import com.example.supermarket.presentation.screen.home.banner.BannerDetailScreen
import com.example.supermarket.presentation.screen.home.product.ProductsWeekSaleScreen
import com.example.supermarket.presentation.screen.map.MapScreen
import com.example.supermarket.presentation.screen.notification.NotificationScreen
import com.example.supermarket.presentation.screen.settings.PrivacyPolicy
import com.example.supermarket.presentation.screen.settings.PushNotification
import com.example.supermarket.presentation.screen.settings.SettingScreen
import com.example.supermarket.presentation.screen.settings.auth.AuthScreen
import com.example.supermarket.presentation.screen.settings.profile.ProfileEditScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {
        composable(Screens.Home.route) {
            HomeScreen(
                onBannerClick = { bannerId ->
                    navController.navigate("banner_detail/$bannerId")
                },
                onClickWeekSale = {
                    navController.navigate("product_week_sale")
                },
                onNavigateToAuth = {
                    navController.navigate(Screens.AuthScreen.route)
                },
                onNotificationClick = {
                    navController.navigate(Screens.Notification.route)
                }

            )
        }
        composable(Screens.AuthScreen.route) {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate(Screens.Profile.route) {
                        popUpTo(Screens.AuthScreen.route) { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Screens.PushNotification.route
        ) {
            PushNotification(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screens.Profile.route) {
            ProfileEditScreen(
                onSaveSuccess = {
                    navController.navigate(Screens.Home.route) {
                        popUpTo(Screens.Profile.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screens.Catalog.route) {
            CatalogScreen(
                onCategoryClick = { id, name ->
                    navController.navigate("${Screens.CatalogById.route}/$id")
                },
                onSearch = {
                    navController.navigate("search")
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = "${Screens.CatalogById.route}/{catalogId}",
            arguments = listOf(navArgument("catalogId") { type = NavType.LongType })
        ) { backStackEntry ->
            val catalogId = backStackEntry.arguments?.getLong("catalogId") ?: 0L
            CatalogByIdScreen(
                catalogId = catalogId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("search") {
            SearchScreen(
                onBack = { navController.popBackStack() },
                onProductClick = { productId ->
                    navController.navigate("product_detail/$productId")
                }
            )
        }

        composable("map") {
            MapScreen()
        }
        composable("settings") {
            SettingScreen(
                onNavigateToEditProfile = { navController.navigate(Screens.Profile.route) },
                onLogoutSuccess = {},
                onNavigateToAuth = {
                    navController.navigate(Screens.AuthScreen.route)
                },
                onPrivacyPolicy = {
                    navController.navigate(Screens.PrivacyPolicy.route)
                },
                onNavigateToAppSettings = {
                    navController.navigate(Screens.PushNotification.route)
                }
            )
        }
        composable(
            route = Screens.PushNotification.route
        ) {
            PushNotification(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screens.Notification.route) {
            NotificationScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable(
            route = Screens.PrivacyPolicy.route
        ) {
            PrivacyPolicy(
                onBack = { navController.popBackStack() }
            )
        }


        composable(
            route = Screens.BannerDetail.route,
            arguments = listOf(navArgument("bannerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bannerId = backStackEntry.arguments?.getLong("bannerId") ?: 0L
            BannerDetailScreen(
                bannerId = bannerId,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screens.ProductWeekSale.route,
        ) {
            ProductsWeekSaleScreen(
                onProductClick = { productId ->
                    navController.navigate("product_detail/$productId")
                },
                onBack = { navController.popBackStack() }
            )
        }

    }

}

sealed class Screens(val route: String) {
    object Home : Screens("home")
    object BannerDetail : Screens("banner_detail/{bannerId}")
    object ProductWeekSale : Screens("product_week_sale")
    object Notification : Screens("notification")
    object CatalogById : Screens("catalog_by_id")
    object PushNotification : Screens("push_notification")
    object AuthScreen : Screens("auth_screen")
    object Profile : Screens("profile")
    object Catalog : Screens("catalog")
    object PrivacyPolicy : Screens("privacy_policy")


}
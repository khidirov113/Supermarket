package com.example.supermarket.presentation.screen.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.supermarket.presentation.screen.auth.AuthScreen
import com.example.supermarket.presentation.screen.banner.BannerDetailScreen
import com.example.supermarket.presentation.screen.productlist.CatalogByIdScreen
import com.example.supermarket.presentation.screen.productlist.SearchScreen
import com.example.supermarket.presentation.screen.notification.NotificationScreen
import com.example.supermarket.presentation.screen.product.ProductsWeekSaleScreen
import com.example.supermarket.presentation.screen.profile.ProfileEditScreen
import com.example.supermarket.presentation.screen.settings.PrivacyPolicy
import com.example.supermarket.presentation.screen.settings.PushNotification

@Composable
fun RootNavGraph(
    rootNavController: NavHostController
) {
    NavHost(
        navController = rootNavController,
        startDestination = Screens.MainFlow.route
    ) {

        composable(Screens.MainFlow.route) {
            MainScreen(rootNavController = rootNavController)
        }

        composable(Screens.AuthScreen.route) {
            AuthScreen(
                onAuthSuccess = {
                    rootNavController.navigate(Screens.Profile.route) {
                        popUpTo(Screens.AuthScreen.route) { inclusive = true }
                    }
                },
                onBack = { rootNavController.popBackStack() }
            )
        }

        composable(route = Screens.PushNotification.route) {
            PushNotification(onBack = { rootNavController.popBackStack() })
        }

        composable(Screens.Profile.route) {
            ProfileEditScreen(
                onSaveSuccess = {
                    rootNavController.navigate(Screens.MainFlow.route) {
                        popUpTo(Screens.Profile.route) { inclusive = true }
                    }
                },
                onNavigateBack = { rootNavController.popBackStack() }
            )
        }

        composable(
            route = "${Screens.CatalogById.route}/{catalogId}",
            arguments = listOf(navArgument("catalogId") { type = NavType.LongType })
        ) { backStackEntry ->
            val catalogId = backStackEntry.arguments?.getLong("catalogId") ?: 0L
            CatalogByIdScreen(
                catalogId = catalogId,
                onBack = { rootNavController.popBackStack() }
            )
        }

        composable(Screens.Search.route) {
            SearchScreen(
                onBack = { rootNavController.popBackStack() },
                onProductClick = { productId ->
                    rootNavController.navigate("product_detail/$productId")
                }
            )
        }

        composable(Screens.Notification.route) {
            NotificationScreen(
                onBack = { rootNavController.popBackStack() },
                onNewsClick = { bannerId ->
                    rootNavController.navigate(
                        Screens.BannerDetail.route.replace("{bannerId}", bannerId.toString())
                    )
                }
            )
        }

        composable(Screens.PrivacyPolicy.route) {
            PrivacyPolicy(onBack = { rootNavController.popBackStack() })
        }

        composable(
            route = Screens.BannerDetail.route,
            arguments = listOf(navArgument("bannerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bannerId = backStackEntry.arguments?.getLong("bannerId") ?: 0L
            BannerDetailScreen(
                bannerId = bannerId,
                onBack = { rootNavController.popBackStack() }
            )
        }

        composable(Screens.ProductWeekSale.route) {
            ProductsWeekSaleScreen(
                onProductClick = { productId ->
                    rootNavController.navigate("product_detail/$productId")
                },
                onBack = { rootNavController.popBackStack() }
            )
        }
    }
}
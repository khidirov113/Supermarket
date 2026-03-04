package com.example.supermarket.presentation.screen.navigation

sealed class Screens(val route: String) {
    data object MainFlow : Screens("main_flow")
    data object Home : Screens("home")
    data object TransactionHistory : Screens("transaction_history")
    data object Catalog : Screens("catalog")
    data object Map : Screens("map")
    data object Settings : Screens("settings")
    data object Search : Screens("search")
    data object CatalogById : Screens("catalog_by_id")
    data object BannerDetail : Screens("banner_detail/{bannerId}")
    data object ProductWeekSale : Screens("product_week_sale")
    data object Notification : Screens("notification")
    data object PushNotification : Screens("push_notification")
    data object AuthScreen : Screens("auth_screen")
    data object Profile : Screens("profile")
    data object About : Screens("about")
    data object PrivacyPolicy : Screens("privacy_policy")
}
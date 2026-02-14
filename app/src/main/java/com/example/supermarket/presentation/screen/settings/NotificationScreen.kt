package com.example.supermarket.presentation.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.supermarket.presentation.ui.theme.White

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
){

    Box(modifier = Modifier.fillMaxWidth().background(White)){

        Text(text = "Notification")

    }
}
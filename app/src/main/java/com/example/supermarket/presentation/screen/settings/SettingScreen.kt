package com.example.supermarket.presentation.screen.settings

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.supermarket.R
import com.example.supermarket.presentation.ui.theme.Black
import com.example.supermarket.presentation.ui.theme.Blue
import com.example.supermarket.presentation.ui.theme.CustomIcons
import com.example.supermarket.presentation.ui.theme.Green
import com.example.supermarket.presentation.ui.theme.Grey200
import com.example.supermarket.presentation.ui.theme.Grey300
import com.example.supermarket.presentation.ui.theme.Red
import com.example.supermarket.presentation.ui.theme.White100
import com.example.supermarket.presentation.ui.theme.White200
import com.example.supermarket.presentation.ui.theme.Yellow
import com.example.supermarket.presentation.utils.AuthBottomSheet

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    onNavigateToEditProfile: () -> Unit,
    onLogoutSuccess: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onNavigateToAppSettings: () -> Unit,
    onPrivacyPolicy: () -> Unit,
    onAboutClick: () -> Unit
) {
    val isAuthenticated by viewModel.isAuthenticated.collectAsState(initial = false)
    val user = viewModel.userData
    val context = LocalContext.current
    var showAuthSheet by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                showLogoutDialog = false
                viewModel.onLogout(onLogoutSuccess)
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    Scaffold(
        containerColor = Color.White,
        topBar = { SettingsTopBar() }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                if (isAuthenticated) {
                    ProfileHeaderCard(
                        name = "${user?.lastName ?: ""} ${user?.firstName ?: ""}".trim(),
                        phone = "+${user?.phone}",
                        onClick = onNavigateToEditProfile
                    )
                } else {
                    BaseSettingRow(
                        text = "Войдите либо \nзарегистрируйтесь",
                        iconRes = R.drawable.ic_user_round,
                        containerColor = White100,
                        iconBgColor = Green,
                        onClick = { showAuthSheet = true }
                    )
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    BaseSettingRow(
                        text = "Настройки",
                        iconRes = R.drawable.ic_settings,
                        iconBgColor = Blue,
                        onClick = onNavigateToAppSettings
                    )
                    BaseSettingRow(
                        text = "Политика конфиденциальности",
                        iconRes = R.drawable.ic_privacy_tip,
                        iconBgColor = Blue,
                        onClick = onPrivacyPolicy
                    )
                }
            }

            item {
                Text(
                    text = "ПРИЛОЖЕНИЕ",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingGroup {
                    val playStoreUrl =
                        "https://play.google.com/store/apps/details?id=tj.tajsoft.bonus"

                    SettingGroupItem(
                        text = "Поделиться приложением",
                        iconRes = R.drawable.ic_share,
                        iconColor = Red,
                        showDivider = true,
                        onClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, playStoreUrl)
                            }
                            context.startActivity(intent)
                        }
                    )
                    SettingGroupItem(
                        text = "Оценить приложение",
                        iconRes = R.drawable.ic_star,
                        iconColor = Yellow,
                        showDivider = true,
                        onClick = {
                            context.startActivity(Intent(Intent.ACTION_VIEW, playStoreUrl.toUri()))
                        }
                    )
                    SettingGroupItem(
                        text = "Свяжитесь с разработчиками",
                        iconRes = R.drawable.ic_messages,
                        iconColor = Blue,
                        showDivider = true,
                        onClick = {
                            onAboutClick()
                        }
                    )
                    SettingGroupItem(
                        text = "О приложении",
                        iconRes = R.drawable.ic_info,
                        iconColor = Green,
                        versionText = "Версия 1.0.6"
                    )
                }
            }

            if (isAuthenticated) {
                item {
                    BaseSettingRow(
                        text = "Выйти",
                        iconRes = R.drawable.ic_logout,
                        iconBgColor = Color.Transparent,
                        iconTintColor = Red,
                        showChevron = false,
                        onClick = { showLogoutDialog = true }
                    )
                }
            }

            item {
                FooterLink(url = "https://tajsoft.tj/")
            }
        }
    }

    if (showAuthSheet) {
        AuthBottomSheet(
            onDismiss = { showAuthSheet = false },
            onLoginClick = {
                showAuthSheet = false
                onNavigateToAuth()
            }
        )
    }
}

@Composable
fun BaseSettingRow(
    text: String,
    iconRes: Int,
    containerColor: Color = Grey300,
    iconBgColor: Color,
    iconTintColor: Color = Color.White,
    showChevron: Boolean = true,
    versionText: String? = null,
    onClick: () -> Unit = {}
) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = Black.copy(alpha = 0.1f)),
                onClick = { onClick() }
            )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = iconTintColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = text,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                lineHeight = 18.sp
            )

            if (versionText != null) {
                Text(
                    text = versionText,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            if (showChevron) {
                Icon(
                    imageVector = CustomIcons.ChevronRight,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun SettingGroup(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Grey300,
    ) {
        Column(content = content)
    }
}

@Composable
fun SettingGroupItem(
    text: String,
    iconRes: Int,
    iconColor: Color,
    showDivider: Boolean = false,
    versionText: String? = null,
    onClick: (() -> Unit)? = null
) {
    val modifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(color = Color.Black.copy(alpha = 0.1f)),
            onClick = onClick
        )
    } else {
        Modifier
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(iconColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = text,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .weight(1f),
                color = Color.Black
            )

            if (versionText != null) {
                Text(text = versionText, color = Color.Gray, fontSize = 12.sp)
            } else {
                Icon(
                    imageVector = CustomIcons.ChevronRight,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.8.dp,
                color = White200
            )
        }
    }
}

@Composable
fun ProfileHeaderCard(
    name: String,
    phone: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Grey300,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = Black.copy(alpha = 0.1f)),
                onClick = { onClick() }
            )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = Color.LightGray
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(text = name, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = phone, color = Color.Gray, fontSize = 14.sp)
            }

            Icon(imageVector = CustomIcons.ChevronRight, contentDescription = null)
        }
    }
}

@Composable
private fun SettingsTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Настройки",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
private fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text("Вы действительно хотите выйти?", fontSize = 16.sp)
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Green
                )
            ) {
                Text(
                    "Да",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Green
                )
            ) {
                Text(
                    "Отмена",
                    color = Green,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun FooterLink(url: String) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Developed by TajSoft Group",
            style = MaterialTheme.typography.labelSmall.copy(textDecoration = TextDecoration.Underline),
            color = Color.LightGray,
            modifier = Modifier.clickable {
                context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PushNotification(
    onBack: () -> Unit,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.processCommand(SettingCommand.SetNotificationEnabled(isGranted))
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Настройки",
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_vector_stroke),
                            contentDescription = null,
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
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            when (val currentState = state.value) {
                is SettingState.Configuration -> {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Grey200
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Push уведомления",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )

                            Switch(
                                checked = currentState.notificationsEnabled,
                                onCheckedChange = { enabled ->
                                    if (enabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                    } else {
                                        viewModel.processCommand(
                                            SettingCommand.SetNotificationEnabled(enabled)
                                        )
                                    }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Green,
                                    uncheckedThumbColor = Color.White,
                                ),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Вы можете управлять разрешениями на уведомления приложений в настройках телефона",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 4.dp),
                        lineHeight = 18.sp
                    )
                }

                SettingState.Initial -> {}
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicy(
    onBack: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Политика конфиденциальности",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_vector_stroke),
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(
                                view: WebView?,
                                url: String?,
                                favicon: Bitmap?
                            ) {
                                super.onPageStarted(view, url, favicon)
                                isLoading = true
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading = false
                            }
                        }
                        loadUrl("https://tajsoft.tj/r_privacy-policy/")
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = Green,
                    strokeWidth = 4.dp
                )
            }
        }
    }
}

package com.example.supermarket.presentation.screen.settings

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.supermarket.R
import com.example.supermarket.presentation.ui.theme.Blue
import com.example.supermarket.presentation.ui.theme.CustomIcons
import com.example.supermarket.presentation.ui.theme.Green
import com.example.supermarket.presentation.ui.theme.Grey
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
) {
    val isAuthenticated by viewModel.isAuthenticated.collectAsState(initial = false)
    val user = viewModel.userData

    val context = LocalContext.current

    var showAuthSheet by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                text = "Настройки",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }

        item {
            if (isAuthenticated) {
                ProfileHeaderCard(
                    name = "${user?.lastName ?: ""} ${user?.firstName ?: ""}".trim(),
                    phone = "+${user?.phone}" ?: " ",
                    onClick = onNavigateToEditProfile
                )
            } else {
                SettingButton(
                    text = "Войдите либо \nзарегистрируйтесь",
                    iconRes = R.drawable.ic_user_round,
                    containerColor = White100,
                    iconBackgroundColor = Green,
                    colorIcon = Green,
                    onClick = { showAuthSheet = true }
                )
            }


            Spacer(modifier = Modifier.height(6.dp))

            SettingButton(
                text = "Настройки",
                iconRes = R.drawable.ic_settings,
                containerColor = Grey,
                iconBackgroundColor = Blue,
                colorIcon = Blue,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(6.dp))

            SettingButton(
                text = "Политика конфиденциальности",
                iconRes = R.drawable.ic_privacy_tip,
                containerColor = Grey,
                iconBackgroundColor = Blue,
                colorIcon = Blue,
                onClick = {
                    val url = "https://tajsoft.tj/r_privacy-policy/"
                    context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                },
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "ПРИЛОЖЕНИЕ",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.padding(start = 8.dp, top = 10.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            SettingGroup {

                SettingGroupItem(
                    text = "Поделиться приложением",
                    iconRes = R.drawable.ic_share,
                    iconColor = Red,
                    showDivider = true,
                    vectorImage = Icons.Default.KeyboardArrowRight,
                    onClick = {
                        val url = "https://play.google.com/store/apps/details?id=tj.tajsoft.bonus"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, url)
                        }
                        context.startActivity(intent)
                    }

                )
                SettingGroupItem(
                    text = "Оценить приложение",
                    iconRes = R.drawable.ic_star,
                    onClick = {
                        val url = "https://play.google.com/store/apps/details?id=tj.tajsoft.bonus"
                        if (url.isNotEmpty()) {
                            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                            context.startActivity(intent)
                        }
                    },
                    iconColor = Yellow,
                    showDivider = true,
                    vectorImage = Icons.Default.KeyboardArrowRight,
                )
                SettingGroupItem(
                    text = "Свяжитесь с разработчиками",
                    iconRes = R.drawable.ic_messages,
                    iconColor = Blue,
                    showDivider = true,
                    vectorImage = Icons.Default.KeyboardArrowRight
                )
                SettingGroupItem(
                    text = "О приложении",
                    iconRes = R.drawable.ic_info,
                    iconColor = Green,
                    showDivider = false,
                    vectorImage = null,
                    versionText = "Версия 1.0.6",
                )
            }
        }
        if (isAuthenticated) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                LogoutButton(onClick = { viewModel.onLogout(onLogoutSuccess) })
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
            FooterLink(url = "https://tajsoft.tj/")
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
fun FooterLink(
    url: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Developed by TajSoft Group",
            style = MaterialTheme.typography.labelSmall.copy(
                textDecoration = TextDecoration.Underline
            ),
            color = Color.LightGray,
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                context.startActivity(intent)
            }
        )
    }
}

@Composable
private fun SettingButton(
    text: String,
    iconRes: Int,
    containerColor: Color,
    iconBackgroundColor: Color,
    colorIcon: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        color = containerColor,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)

    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = iconBackgroundColor,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(30.dp)
            ) {
                Box(
                    modifier = Modifier.background(colorIcon),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = Color.White,
                    )
                }
            }
            Text(
                text = text,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                )
            )
            Icon(
                imageVector = CustomIcons.ChevronRight,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
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
        shape = RoundedCornerShape(12.dp),
        color = Grey,
    ) {
        Column(content = content)
    }
}

@Composable
fun SettingGroupItem(
    text: String,
    iconRes: Int,
    iconColor: Color,
    vectorImage: ImageVector?,
    showDivider: Boolean,
    versionText: String? = null,
    onClick: () -> Unit = {}
) {
    Column(modifier = Modifier.clickable {

        onClick()
    }) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                color = iconColor,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(30.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Text(
                text = text,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )

            if (versionText != null) {
                Text(
                    text = versionText,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            if (vectorImage != null) {
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
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFF7F7F7),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = Color.LightGray
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
                Text(
                    text = phone,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray
                    )
                )
            }
        }
    }
}

@Composable
fun LogoutButton(onClick: () -> Unit) {
    SettingButton(
        text = "Выйти",
        iconRes = R.drawable.ic_logout,
        containerColor = Color(0xFFFFF5F5),
        iconBackgroundColor = Color.White,
        colorIcon = Red,
        onClick = onClick
    )
}



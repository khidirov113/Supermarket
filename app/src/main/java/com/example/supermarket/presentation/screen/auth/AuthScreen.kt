package com.example.supermarket.presentation.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.supermarket.presentation.ui.theme.Green
import com.example.supermarket.presentation.ui.theme.Grey100
import com.example.supermarket.presentation.ui.theme.Grey200
import com.example.supermarket.presentation.utils.PhoneVisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (viewModel.step == 1) onBack() else viewModel.step = 1
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewModel.step == 1) {
                PhoneInputStep(viewModel)
            } else {
                OtpInputStep(viewModel, onAuthSuccess)
            }
        }
    }
}

@Composable
fun ColumnScope.PhoneInputStep(viewModel: AuthViewModel) {
    Text(
        "Войдите в систему",
        fontSize = 24.sp,
        color = Color.Black,
        fontWeight = FontWeight.Bold
    )
    Text(
        "Введите номер телефона для входа",
        color = Color.Gray,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(vertical = 8.dp)
    )

    Spacer(modifier = Modifier.height(32.dp))

    OutlinedTextField(
        value = viewModel.phoneInput,
        onValueChange = { newValue ->
            viewModel.clearError()
            val digitsOnly = newValue.filter { it.isDigit() }
            if (digitsOnly.length <= 9) {
                viewModel.phoneInput = digitsOnly
            }
        },
        visualTransformation = remember { PhoneVisualTransformation() },
        modifier = Modifier.fillMaxWidth(),
        prefix = { Text("+992 ", color = Color.Black, fontWeight = FontWeight.Medium) },
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = viewModel.errorMessage != null,
        supportingText = {
            if (viewModel.errorMessage != null) {
                Text(
                    text = viewModel.errorMessage ?: "Пользователя с таким номером телефона не существует",
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Grey100,
            cursorColor = Green,
            unfocusedBorderColor = Grey100,
            errorBorderColor = Color.Red,
            errorCursorColor = Color.Red,
            errorPrefixColor = Color.Black,
            errorSupportingTextColor = Color.Red
        )
    )

    Spacer(modifier = Modifier.weight(1f))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .padding(bottom = 24.dp),
    ) {
        Button(
            onClick = { viewModel.onSendCodeClick() },
            enabled = viewModel.isPhoneValid && !viewModel.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 10.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (viewModel.isPhoneValid) Green else Grey200,
                contentColor = if (viewModel.isPhoneValid) Color.White else Color.Gray
            )
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Войти")
            }
        }
    }
}

@Composable
fun ColumnScope.OtpInputStep(viewModel: AuthViewModel, onAuthSuccess: () -> Unit) {
    val maskedPhone = remember(viewModel.phoneInput) {
        val phone = viewModel.phoneInput
        if (phone.length == 9) {
            "+992 ${phone.substring(0, 2)} *** ** ${phone.substring(7, 9)}"
        } else {
            "+992 $phone"
        }
    }

    Text(
        text = "Код подтверждения",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )

    Text(
        text = "Мы отправили код на номер\n$maskedPhone",
        color = Color.Gray,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
    )

    Spacer(modifier = Modifier.height(32.dp))

    Box(contentAlignment = Alignment.Center) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            repeat(4) { index ->
                val char = viewModel.code.getOrNull(index)?.toString() ?: ""
                val isFocused = viewModel.code.length == index

                val borderColor = when {
                    viewModel.errorMessage != null -> Color.Red
                    char.isNotEmpty() || isFocused -> Green
                    else -> Color.Transparent
                }

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Grey200, RoundedCornerShape(12.dp))
                        .border(1.5.dp, borderColor, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = char,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }

        BasicTextField(
            value = viewModel.code,
            onValueChange = {
                if (it.length <= 4) {
                    viewModel.code = it
                    viewModel.clearError()
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0f)
        )
    }
    if (viewModel.errorMessage != null) {
        Text(
            text = viewModel.errorMessage ?: "",
            color = Color.Red,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
    }

    Spacer(modifier = Modifier.weight(1f))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
    ) {
        Button(
            onClick = { viewModel.onVerifyCodeClick(onAuthSuccess) },
            enabled = viewModel.code.length == 4 && !viewModel.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (viewModel.code.length == 4) Green else Grey200,
                contentColor = if (viewModel.code.length == 4) Color.White else Color.Gray
            )
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("Далее")
            }
        }

        val isTimerFinished = viewModel.timerSeconds == 0

        Button(
            onClick = {
                if (isTimerFinished) {
                    viewModel.onSendCodeClick()
                    viewModel.startTimer()
                }
            },
            enabled = isTimerFinished,
            modifier = Modifier
                .padding(top = 12.dp, bottom = 12.dp)
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isTimerFinished) Green else Grey200,
                contentColor = if (isTimerFinished) Color.White else Color.Gray,
                disabledContainerColor = Grey200,
                disabledContentColor = Color.Gray
            )
        ) {
            val time = String.format("00:%02d", viewModel.timerSeconds)
            Text(
                text = if (isTimerFinished) "Отправить код еще раз" else "Новый код через $time сек.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
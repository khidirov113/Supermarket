//package com.example.supermarket.presentation.screen.settings.auth
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.SpanStyle
//import androidx.compose.ui.text.buildAnnotatedString
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.withStyle
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//
//private val BrandGreen = Color(0xFF00C853)
//private val TextGray = Color(0xFF9E9E9E)
//private val InputBg = Color(0xFFF5F5F5)
//
//@Composable
//fun AuthScreen(
//    viewModel: AuthViewModel = hiltViewModel(),
//    onSuccess: () -> Unit,
//    onBack: () -> Unit
//) {
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//
//    LaunchedEffect(uiState.isAuthorized) {
//        if (uiState.isAuthorized) {
//            onSuccess()
//        }
//    }
//
//    Scaffold(
//        modifier = Modifier.imePadding(),
//        containerColor = Color.White
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
//            if (!uiState.isCodeSent) {
//                PhoneInputContent(
//                    phone = uiState.phone,
//                    isLoading = uiState.isLoading,
//                    onPhoneChanged = viewModel::onPhoneChange,
//                    onContinueClicked = viewModel::sendCode
//                )
//            } else {
//                OtpInputContent(
//                    phone = uiState.phone,
//                    code = uiState.code,
//                    isLoading = uiState.isLoading,
//                    onCodeChanged = viewModel::onCodeChange,
//                    onVerifyClicked = viewModel::verifyCode,
//                    onResendClicked = {  }
//                )
//            }
//
//            if (uiState.errorMessage != null) {
//                Snackbar(
//                    modifier = Modifier
//                        .align(Alignment.BottomCenter)
//                        .padding(16.dp),
//                    containerColor = MaterialTheme.colorScheme.error
//                ) {
//                    Text(text = uiState.errorMessage!!)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun PhoneInputContent(
//    phone: String,
//    isLoading: Boolean,
//    onPhoneChanged: (String) -> Unit,
//    onContinueClicked: () -> Unit
//) {
//    val isPhoneValid = phone.length == 9
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(horizontal = 24.dp, vertical = 16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
//             IconButton(onClick = { }) {
//                 Icon(Icons.Default.ArrowBack, contentDescription = null)
//             }
//        }
//
//        Spacer(modifier = Modifier.height(40.dp))
//
//        Text(
//            text = "Войдите в систему",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color.Black
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Text(
//            text = "Введите номер телефона\nдля входа",
//            fontSize = 16.sp,
//            color = TextGray,
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(modifier = Modifier.height(48.dp))
//
//        OutlinedTextField(
//            value = phone,
//            onValueChange = { if (it.length <= 9 && it.all { char -> char.isDigit() }) onPhoneChanged(it) },
//            modifier = Modifier.fillMaxWidth(),
//            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
//            prefix = {
//                Text(text = "+992 ", fontSize = 18.sp, color = Color.Black)
//            },
//            shape = RoundedCornerShape(12.dp),
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedBorderColor = BrandGreen,
//                unfocusedBorderColor = Color.LightGray,
//                cursorColor = BrandGreen
//            ),
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            singleLine = true,
//            enabled = !isLoading
//        )
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        Button(
//            onClick = onContinueClicked,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(56.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = BrandGreen,
//                disabledContainerColor = BrandGreen.copy(alpha = 0.5f)
//            ),
//            shape = RoundedCornerShape(12.dp),
//            enabled = isPhoneValid && !isLoading
//        ) {
//            if (isLoading) {
//                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
//            } else {
//                Text(text = "Войти", fontSize = 18.sp, fontWeight = FontWeight.Medium)
//            }
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//    }
//}
//
//@Composable
//fun OtpInputContent(
//    phone: String,
//    code: String,
//    isLoading: Boolean,
//    onCodeChanged: (String) -> Unit,
//    onVerifyClicked: () -> Unit,
//    onResendClicked: () -> Unit
//) {
//    val isCodeValid = code.length == 4
//
//    val maskedPhone = remember(phone) {
//        if (phone.length == 9) {
//            "+ 992 ${phone.substring(0, 2)} *** ** ${phone.substring(7, 9)}"
//        } else {
//            "+ 992 $phone"
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(horizontal = 24.dp, vertical = 16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(40.dp))
//        Text(
//            text = "Код подтверждения",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color.Black
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//
//        val annotatedString = buildAnnotatedString {
//            append("Мы отправили код на номер\n")
//            withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, color = Color.Black)) {
//                append(maskedPhone)
//            }
//        }
//        Text(
//            text = annotatedString,
//            fontSize = 16.sp,
//            color = TextGray,
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(modifier = Modifier.height(48.dp))
//
//        OtpInputField(
//            code = code,
//            onCodeChanged = onCodeChanged,
//            enabled = !isLoading
//        )
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        Button(
//            onClick = onVerifyClicked,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(56.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = BrandGreen,
//                disabledContainerColor = BrandGreen.copy(alpha = 0.5f)
//            ),
//            shape = RoundedCornerShape(12.dp),
//            enabled = isCodeValid && !isLoading
//        ) {
//            if (isLoading) {
//                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
//            } else {
//                Text(text = "Далее", fontSize = 18.sp, fontWeight = FontWeight.Medium)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        TextButton(
//            onClick = onResendClicked,
//            enabled = false,
//            colors = ButtonDefaults.textButtonColors(disabledContentColor = TextGray)
//        ) {
//            Text(text = "Новый код через 00:56 сек.", fontSize = 16.sp)
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//    }
//}
//
//@Composable
//fun OtpInputField(
//    code: String,
//    onCodeChanged: (String) -> Unit,
//    enabled: Boolean
//) {
//    BasicTextField(
//        value = code,
//        onValueChange = {
//            if (it.length <= 4 && it.all { char -> char.isDigit() }) {
//                onCodeChanged(it)
//            }
//        },
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
//        enabled = enabled,
//        decorationBox = {
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                repeat(4) { index ->
//                    val char = when {
//                        index >= code.length -> ""
//                        else -> code[index].toString()
//                    }
//                    val isFocused = code.length == index
//
//                    Box(
//                        modifier = Modifier
//                            .size(64.dp) // Размер квадрата
//                            .clip(RoundedCornerShape(12.dp))
//                            .background(InputBg)
//                            .border(
//                                width = if (isFocused) 2.dp else 0.dp,
//                                color = if (isFocused) BrandGreen else Color.Transparent,
//                                shape = RoundedCornerShape(12.dp)
//                            ),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = char,
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color.Black
//                        )
//                    }
//                }
//            }
//        }
//    )
//}
//
//@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
//@Composable
//fun PhoneInputPreview() {
//
//    PhoneInputContent(phone = "9296634", isLoading = false, onPhoneChanged = {}, onContinueClicked = {})
//}
//
//@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
//@Composable
//fun OtpInputPreview() {
//    OtpInputContent(phone = "929663474", code = "12", isLoading = false, onCodeChanged = {}, onVerifyClicked = {}, onResendClicked = {})
//}
//package com.example.supermarket.presentation.screen.settings.auth
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.imePadding
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Check
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.Refresh
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.CenterAlignedTopAppBar
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.DatePicker
//import androidx.compose.material3.DatePickerDefaults
//import androidx.compose.material3.DatePickerDialog
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.OutlinedTextFieldDefaults
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.material3.rememberDatePickerState
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
//import com.example.supermarket.R
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EditProfileScreen(
//    onBack: () -> Unit,
//    onLogoutSuccess: () -> Unit,
//    viewModel: ProfileViewModel = hiltViewModel()
//) {
//    if (viewModel.isDatePickerOpen) {
//        val datePickerState = rememberDatePickerState(
//            initialSelectedDateMillis = viewModel.birthDateMillis
//        )
//
//        DatePickerDialog(
//            onDismissRequest = { viewModel.isDatePickerOpen = false },
//            confirmButton = {
//                TextButton(onClick = {
//                    // МУҲИМ: Сана танланганда State янгиланади
//                    viewModel.birthDateMillis = datePickerState.selectedDateMillis
//                    viewModel.isDatePickerOpen = false
//                }) {
//                    Text("Применить", color = Color(0xFF00C853), fontWeight = FontWeight.Bold)
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { viewModel.isDatePickerOpen = false }) {
//                    Text("Отмена", color = Color.Gray)
//                }
//            }
//        ) {
//            DatePicker(
//                state = datePickerState,
//                colors = DatePickerDefaults.colors(
//                    selectedDayContainerColor = Color(0xFF00C853),
//                    todayContentColor = Color(0xFF00C853),
//                    todayDateBorderColor = Color(0xFF00C853)
//                )
//            )
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text("Редактировать профиль", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null, modifier = Modifier.size(32.dp))
//                    }
//                },
//                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
//            )
//        },
//        containerColor = Color.White
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .padding(horizontal = 24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // Профиль расми ва "Refresh/Add" иконкаси
//            Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.padding(top = 20.dp, bottom = 40.dp)) {
//                Box(
//                    modifier = Modifier.size(120.dp).background(Color(0xFFF5F5F5), CircleShape),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(Icons.Default.Person, null, modifier = Modifier.size(80.dp), tint = Color(0xFFE0E0E0))
//                }
//                // Расмни янгилаш тугмаси
//                Box(
//                    modifier = Modifier
//                        .size(34.dp)
//                        .background(Color.White, CircleShape)
//                        .padding(2.dp)
//                        .shadow(2.dp, CircleShape)
//                        .clickable { /* Расмни юклаш логикаси шу ерда бўлади */ },
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(Icons.Default.Add, null, modifier = Modifier.size(20.dp), tint = Color.LightGray)
//                }
//            }
//
//            // Исм ва Фамилия
//            ProfileInputField(value = viewModel.firstName, onValueChange = { viewModel.firstName = it }, label = "Введите имя")
//            Spacer(modifier = Modifier.height(16.dp))
//            ProfileInputField(value = viewModel.lastName, onValueChange = { viewModel.lastName = it }, label = "Введите фамилию")
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // --- САНА ТАНЛАШ (Календарь тузатилган қисми) ---
//            Box(modifier = Modifier.fillMaxWidth()) {
//                OutlinedTextField(
//                    value = viewModel.formattedDate,
//                    onValueChange = {}, // ReadOnly
//                    modifier = Modifier.fillMaxWidth(),
//                    enabled = false, // Фокус бўлмаслиги учун
//                    readOnly = true,
//                    shape = RoundedCornerShape(16.dp),
//                    trailingIcon = {
//                        Icon(painterResource(R.drawable.ic_calendar), null, tint = Color(0xFF00C853), modifier = Modifier.size(24.dp))
//                    },
//                    colors = OutlinedTextFieldDefaults.colors(
//                        disabledTextColor = Color.Black,
//                        disabledBorderColor = Color(0xFFE0E0E0),
//                        disabledContainerColor = Color.White,
//                        disabledPlaceholderColor = Color.Gray,
//                        disabledTrailingIconColor = Color(0xFF00C853)
//                    )
//                )
//                // Кўринмас қатлам - босишни ушлаб олади ва Календарни очади
//                Box(
//                    modifier = Modifier
//                        .matchParentSize()
//                        .clickable { viewModel.isDatePickerOpen = true }
//                )
//            }
//
//            // Жинс танлаш
//            Row(modifier = Modifier.fillMaxWidth().padding(top = 24.dp)) {
//                GenderRadioButton("Мужской", isSelected = viewModel.gender == "male") { viewModel.gender = "male" }
//                Spacer(modifier = Modifier.width(32.dp))
//                GenderRadioButton("Женский", isSelected = viewModel.gender == "female") { viewModel.gender = "female" }
//            }
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            // Сохранить тугмаси
//            Button(
//                onClick = { viewModel.onSave { onBack() } },
//                modifier = Modifier.fillMaxWidth().height(56.dp).imePadding().padding(bottom = 10.dp),
//                shape = RoundedCornerShape(16.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
//            ) {
//                if (viewModel.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
//                else Text("Сохранить", fontSize = 16.sp, fontWeight = FontWeight.Bold)
//            }
//
//            // Выйти (Logout) тугмаси (ихтиёрий, агар керак бўлса)
//            TextButton(
//                onClick = { viewModel.onLogout { onLogoutSuccess() } },
//                modifier = Modifier.padding(bottom = 16.dp)
//            ) {
//                Text("Выйти из профиля", color = Color.Red)
//            }
//        }
//    }
//}
//
//// Ёрдамчи компонентлар
//@Composable
//fun ProfileInputField(value: String, onValueChange: (String) -> Unit, label: String) {
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        placeholder = { Text(label, color = Color.LightGray) },
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(16.dp),
//        colors = OutlinedTextFieldDefaults.colors(
//            focusedContainerColor = Color.White,
//            unfocusedContainerColor = Color.White,
//            focusedBorderColor = Color(0xFFE0E0E0),
//            unfocusedBorderColor = Color(0xFFE0E0E0),
//            focusedTextColor = Color.Black,
//            unfocusedTextColor = Color.Black
//        )
//    )
//}
//
//@Composable
//fun GenderRadioButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
//    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onClick() }) {
//        Box(
//            modifier = Modifier.size(24.dp).background(if (isSelected) Color(0xFF00C853) else Color(0xFFF5F5F5), CircleShape),
//            contentAlignment = Alignment.Center
//        ) {
//            if (isSelected) Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
//        }
//        Text(text, modifier = Modifier.padding(start = 12.dp), fontSize = 16.sp, fontWeight = FontWeight.Medium)
//    }
//}
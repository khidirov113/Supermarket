package com.example.supermarket.domain.entity

data class Notification(
    val id: Int,
    val title: String,
    val description: String,
    val createdAt: String,
    val iconPath: String
)
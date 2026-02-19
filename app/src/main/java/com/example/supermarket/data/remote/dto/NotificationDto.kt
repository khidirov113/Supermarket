package com.example.supermarket.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationResponse(
    val data: List<NotificationDto>
)
@Serializable
data class NotificationDto(
    val id: Int,
    val title: String,
    val description: String? = "",
    @SerialName("created_at") val createdAt: String,
    @SerialName("image_path") val iconPath: String? = ""
)


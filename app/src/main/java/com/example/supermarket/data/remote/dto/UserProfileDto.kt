package com.example.supermarket.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    @SerialName("name") val name: String? = null,
    @SerialName("surname") val surname: String? = null,
    @SerialName("born_in") val bornIn: String? = null,
    @SerialName("gender") val gender: Int? = null,
    @SerialName("image_path") val imagePath: String? = null,

    @SerialName("balance") val balance: Double? = null,
    @SerialName("notifications_count") val notificationsCount: Int? = null,
    @SerialName("phone") val phone: String? = null
)
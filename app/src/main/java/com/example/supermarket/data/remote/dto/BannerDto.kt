package com.example.supermarket.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BannerDto(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String?,
    @SerialName("image_path")
    val image: String?,
    @SerialName("description")
    val description: String?,
)
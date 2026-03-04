package com.example.supermarket.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AboutDto(
    @SerialName("id")
    val id: Long = 0,
    @SerialName("description")
    val description: String? = "",
    @SerialName("images")
    val images: List<ImageDto>? = emptyList(),
    val phone: String? = "",
    @SerialName("telegram")
    val telegram: String? = "",
    @SerialName("title")
    val title: String? = "",
    @SerialName("whatsapp")
    val whatsapp: String? = ""
)

@Serializable
data class ImageDto(
    @SerialName("id")
    val id: Long? = null,
    @SerialName("image_path")
    val imagePath: String? = null
)
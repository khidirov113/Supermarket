package com.example.supermarket.data.mapper

import com.example.supermarket.data.remote.dto.AccessCodeDto
import com.example.supermarket.data.remote.dto.BannerDto
import com.example.supermarket.data.remote.dto.CategoryDto
import com.example.supermarket.data.remote.dto.NotificationDto
import com.example.supermarket.data.remote.dto.ProductDto
import com.example.supermarket.data.remote.dto.StoreDto
import com.example.supermarket.data.remote.dto.SubCategoryDto
import com.example.supermarket.data.remote.dto.UserProfileDto
import com.example.supermarket.domain.entity.Banner
import com.example.supermarket.domain.entity.Category
import com.example.supermarket.domain.value.Notification
import com.example.supermarket.domain.entity.Product
import com.example.supermarket.domain.value.QrCodeData
import com.example.supermarket.domain.value.Store
import com.example.supermarket.domain.entity.SubCategory
import com.example.supermarket.domain.value.UserProfile


fun BannerDto.toDomain() = Banner(
    id = this.id,
    title = this.title ?: "",
    image = "https://market.tajsoft.tj/" + this.image,
    description = this.description ?: "",
)

fun NotificationDto.toDomain() = Notification(
    id = id,
    title = title,
    description = description ?: "",
    createdAt = createdAt,
    iconPath = "https://market.tajsoft.tj$iconPath"
)


fun ProductDto.toDomain(): Product {
    val baseUrl = "https://market.tajsoft.tj/"
    val imageList = this.images?.map { dtoImage ->
        "$baseUrl${dtoImage.imagePath}"
    } ?: emptyList()
    return Product(
        id = this.id ?: 0,
        title = this.title ?: "",
        description = this.description ?: "",
        inStock = (this.inStock ?: 0) == 1,
        price = this.price ?: 0.0,
        unit = this.unit ?: "",
        salePercent = this.salePercent ?: 0.0,
        salePrice = this.finalPrice ?: (this.price ?: 0.0),
        image = if (this.image != null) baseUrl + this.image else "",
        images = imageList
    )
}

fun SubCategoryDto.toDomain() = SubCategory(
    id = id ?: 0,
    title = title ?: "",
    categoryId = categoryId ?: 0,
    products = products?.map { it.toDomain() } ?: emptyList()
)

fun CategoryDto.toDomain() = Category(
    id = id,
    title = title,
    image = image?.let { "https://market.tajsoft.tj$it" },
    productsCount = productsCount,
    subcategories = subcategories?.map { it.toDomain() } ?: emptyList()
)

fun UserProfileDto.toDomain(): UserProfile {
    return UserProfile(
        firstName = this.name ?: "",
        lastName = this.surname ?: "",
        birthDate = this.bornIn ?: "",
        gender = this.gender ?: 1,
        image = this.imagePath,
        bonus = this.balance ?: 0.0,
        phone = this.phone ?: ""
    )
}

fun AccessCodeDto.toDomain(): QrCodeData {
    return QrCodeData(
        code = this.code ?: "0000",
        expiresAt = this.expiresAt
    )
}


fun StoreDto.toDomain() = Store(
    name = name,
    latitude = lat,
    longitude = lng,
    address = address ?: ""
)
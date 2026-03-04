package com.example.supermarket.data.mapper

import com.example.supermarket.data.remote.dto.AboutDto
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
import com.example.supermarket.data.remote.dto.TransactionDto
import com.example.supermarket.domain.value.About
import com.example.supermarket.domain.value.Transaction
import java.text.SimpleDateFormat
import java.util.Locale

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
        phone = this.phone ?: "",
        notificationsCount = this.notificationsCount
    )
}

fun AccessCodeDto.toDomain(): QrCodeData {
    return QrCodeData(
        code = this.code ?: "0000",
        expiresAt = this.expiresAt
    )
}


fun AboutDto.toDomain(): About {
    val firstImageObj = this.images?.firstOrNull()

    val imagePath = firstImageObj?.imagePath ?: ""

    val fullImageUrl = if (imagePath.isNotEmpty()) {
        "https://market.tajsoft.tj$imagePath"
    } else {
        ""
    }

    return About(
        id = this.id,
        title = this.title ?: "",
        description = this.description ?: "",
        phone = this.phone ?: "",
        telegram = this.telegram ?: "",
        whatsapp = this.whatsapp ?: "",
        images = fullImageUrl
    )
}

fun StoreDto.toDomain(): Store {
    val formatTime: (String?) -> String = { time ->
        if (time != null && time.length >= 5) time.substring(0, 5) else ""
    }

    return Store(
        id = this.id,
        title = this.title,
        latitude = this.latitude.toDoubleOrNull() ?: 0.0,
        longitude = this.longitude.toDoubleOrNull() ?: 0.0,
        address = this.address ?: "Не указан",
        fromTime = formatTime(this.fromTime),
        toTime = formatTime(this.toTime),
        images = this.images ?: emptyList()
    )
}

fun TransactionDto.toDomain(): Transaction {
    var formattedDate = ""
    var formattedTime = ""

    try {
        if (!this.createdAt.isNullOrEmpty()) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val dateObj = inputFormat.parse(this.createdAt.substringBefore("."))
            if (dateObj != null) {
                formattedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(dateObj)
                formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(dateObj)
            }
        }
    } catch (e: Exception) {
        formattedDate = this.createdAt?.substringBefore("T") ?: ""
        formattedTime = this.createdAt?.substringAfter("T")?.substringBefore(".") ?: ""
    }

    val isDeposit = this.type == "deposit" || (this.amount ?: 0.0) > 0

    return Transaction(
        id = this.id,
        title = this.title ?: if (isDeposit) "Начисление" else "Снятие",
        bonusAmount = this.amount ?: 0.0,
        price = this.price ?: 0.0,
        date = formattedDate,
        time = formattedTime,
        isPositive = isDeposit
    )
}
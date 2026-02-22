package com.example.supermarket.domain.value

data class Settings(
    val notificationsEnabled : Boolean
){
    companion object{
        const val DEFAULT_NOTIFICATIONS_ENABLED = false
    }
}
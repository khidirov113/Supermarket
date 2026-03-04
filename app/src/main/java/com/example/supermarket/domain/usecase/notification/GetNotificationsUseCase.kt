package com.example.supermarket.domain.usecase.notification

import com.example.supermarket.domain.value.Notification
import com.example.supermarket.domain.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): List<Notification> {
        return repository.getNotifications()
    }
}
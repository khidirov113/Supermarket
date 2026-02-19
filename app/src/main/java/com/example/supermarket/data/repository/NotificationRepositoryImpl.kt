package com.example.supermarket.data.repository

import com.example.supermarket.data.mapper.toDomain
import com.example.supermarket.data.remote.network.NotificationApi
import com.example.supermarket.domain.entity.Notification
import com.example.supermarket.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val api: NotificationApi
) : NotificationRepository {

    override suspend fun getNotifications(): Result<List<Notification>> {
        return runCatching {
            api.getNotifications().data.map { it.toDomain() }
        }
    }


    override suspend fun getNews(): Result<List<Notification>> {
        return runCatching {
            api.getNews().data.map { it.toDomain() }
        }
    }


}
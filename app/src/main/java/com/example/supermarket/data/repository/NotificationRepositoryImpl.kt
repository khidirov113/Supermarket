package com.example.supermarket.data.repository

import com.example.supermarket.data.mapper.toDomain
import com.example.supermarket.data.remote.network.NotificationApi
import com.example.supermarket.domain.error.AppException
import com.example.supermarket.domain.value.Notification
import com.example.supermarket.domain.repository.NotificationRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val api: NotificationApi
) : NotificationRepository {

    override suspend fun getNotifications(): List<Notification> {
        return try {
            api.getNotifications().data.map { it.toDomain() }
        } catch (e: IOException) {
            throw AppException.NetworkException()
        } catch (e: HttpException) {
            throw AppException.ServerException(e.code())
        } catch (e: Exception) {
            throw AppException.UnknownException()
        }
    }

    override suspend fun getNews(): List<Notification> {
        return try {
            api.getNews().data.map { it.toDomain() }
        } catch (e: IOException) {
            throw AppException.NetworkException()
        } catch (e: HttpException) {
            throw AppException.ServerException(e.code())
        } catch (e: Exception) {
            throw AppException.UnknownException()
        }
    }


}
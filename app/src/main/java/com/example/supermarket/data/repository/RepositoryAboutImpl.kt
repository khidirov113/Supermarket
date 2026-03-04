package com.example.supermarket.data.repository

import com.example.supermarket.data.mapper.toDomain
import com.example.supermarket.data.remote.network.AboutApi
import com.example.supermarket.domain.error.AppException
import com.example.supermarket.domain.repository.RepositoryAbout
import com.example.supermarket.domain.value.About
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RepositoryAboutImpl @Inject constructor(
    private val aboutApi: AboutApi
) : RepositoryAbout {
    override suspend fun getAbout(): About {
        return try {
            aboutApi.getAbout().toDomain()
        } catch (e: IOException) {
            throw AppException.NetworkException()
        } catch (e: HttpException) {
            throw AppException.ServerException(e.code())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw AppException.UnknownException()
        }
    }
}
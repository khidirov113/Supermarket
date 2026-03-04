package com.example.supermarket.domain.repository

import com.example.supermarket.domain.value.About

interface RepositoryAbout {
    suspend fun getAbout(): About
}
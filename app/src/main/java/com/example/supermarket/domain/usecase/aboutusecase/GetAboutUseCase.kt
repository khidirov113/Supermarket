package com.example.supermarket.domain.usecase.aboutusecase

import com.example.supermarket.domain.repository.RepositoryAbout
import com.example.supermarket.domain.value.About
import javax.inject.Inject

class GetAboutUseCase @Inject constructor(
    private val repositoryAbout: RepositoryAbout
) {
    suspend fun invoke(id: Long): About {
        return repositoryAbout.getAbout()
    }
}
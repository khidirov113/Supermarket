package com.example.supermarket.domain.usecase.user

import com.example.supermarket.domain.repository.ProfileRepository
import javax.inject.Inject


class UpdateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        name: String?,
        surname: String?,
        bornIn: String?,
        gender: Int?,
        imagePath: String?
    ) {
        return repository.updateProfile(name, surname, bornIn, gender, imagePath)
    }
}
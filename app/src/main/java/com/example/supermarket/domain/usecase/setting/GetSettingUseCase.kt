package com.example.supermarket.domain.usecase.setting

import com.example.supermarket.domain.repository.SettingRepository
import javax.inject.Inject

class GetSettingUseCase @Inject constructor(
    private val settingRepository: SettingRepository
) {

    operator fun invoke() = settingRepository.getSettings()
}
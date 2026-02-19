package com.example.supermarket.domain.usecase.setting


import com.example.supermarket.domain.repository.SettingRepository
import javax.inject.Inject

class UpdateNotificationsEnabledUseCase @Inject constructor(
    private val settingRepository: SettingRepository
) {

    suspend operator fun invoke(enabled: Boolean) {
        settingRepository.updateNotificationEnabled(enabled)
    }
}
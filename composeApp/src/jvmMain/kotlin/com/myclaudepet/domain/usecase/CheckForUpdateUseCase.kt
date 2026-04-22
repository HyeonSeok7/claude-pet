package com.myclaudepet.domain.usecase

import com.myclaudepet.domain.model.AppUpdate
import com.myclaudepet.domain.repository.UpdateSource

class CheckForUpdateUseCase(private val source: UpdateSource) {
    suspend operator fun invoke(): AppUpdate? = source.latest()
}

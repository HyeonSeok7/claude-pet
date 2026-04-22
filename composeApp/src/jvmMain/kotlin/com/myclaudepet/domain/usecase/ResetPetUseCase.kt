package com.myclaudepet.domain.usecase

import com.myclaudepet.domain.repository.PetRepository

class ResetPetUseCase(private val repository: PetRepository) {
    suspend operator fun invoke() {
        repository.resetProgress()
    }
}

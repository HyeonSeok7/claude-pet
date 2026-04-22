package com.myclaudepet.domain.usecase

import com.myclaudepet.domain.repository.PetRepository

class InteractWithPetUseCase(private val repository: PetRepository) {
    suspend operator fun invoke() {
        val pet = repository.current()
        repository.updateSatiation(pet.satiation + CLICK_FEED)
        repository.updateAffinity(pet.affinity + CLICK_AFFINITY)
    }

    private companion object {
        const val CLICK_FEED = 2.0
        const val CLICK_AFFINITY = 2
    }
}

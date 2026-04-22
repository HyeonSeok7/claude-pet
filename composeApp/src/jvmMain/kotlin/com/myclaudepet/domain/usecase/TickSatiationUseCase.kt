package com.myclaudepet.domain.usecase

import com.myclaudepet.domain.repository.PetRepository

class TickSatiationUseCase(private val repository: PetRepository) {
    suspend operator fun invoke() {
        val pet = repository.current()
        repository.updateSatiation(pet.satiation - TICK_DRAIN)
    }

    private companion object {
        const val TICK_DRAIN = 1.0
    }
}

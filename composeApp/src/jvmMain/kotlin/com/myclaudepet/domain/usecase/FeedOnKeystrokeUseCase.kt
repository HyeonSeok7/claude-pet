package com.myclaudepet.domain.usecase

import com.myclaudepet.domain.repository.PetRepository

class FeedOnKeystrokeUseCase(private val repository: PetRepository) {
    suspend operator fun invoke() {
        val pet = repository.current()
        repository.incrementKeystrokes()
        repository.updateSatiation(pet.satiation + KEY_FEED)
        repository.updateAffinity(pet.affinity + KEY_AFFINITY)
    }

    private companion object {
        const val KEY_FEED = 0.05
        const val KEY_AFFINITY = 1
    }
}

package com.myclaudepet.domain.usecase

import com.myclaudepet.domain.model.PetPosition
import com.myclaudepet.domain.repository.PetRepository

class MovePetUseCase(private val repository: PetRepository) {
    suspend operator fun invoke(position: PetPosition) {
        repository.updatePosition(position)
    }
}

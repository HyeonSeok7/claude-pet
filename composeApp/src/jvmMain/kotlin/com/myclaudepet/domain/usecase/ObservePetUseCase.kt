package com.myclaudepet.domain.usecase

import com.myclaudepet.domain.model.Pet
import com.myclaudepet.domain.repository.PetRepository
import kotlinx.coroutines.flow.Flow

class ObservePetUseCase(private val repository: PetRepository) {
    operator fun invoke(): Flow<Pet> = repository.observe()
}

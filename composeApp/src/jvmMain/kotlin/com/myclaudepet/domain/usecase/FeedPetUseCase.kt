package com.myclaudepet.domain.usecase

import com.myclaudepet.domain.model.Affinity
import com.myclaudepet.domain.model.PetAnimationState
import com.myclaudepet.domain.model.Satiation
import com.myclaudepet.domain.repository.PetRepository

/**
 * 수동 "밥 주기" 액션. 포만감을 최대치로 올리고 호감도를 가산한 뒤
 * `Fed` 상태로 잠시 유지. `Fed → Default` 복귀는 StateHolder 가 타이머로 처리.
 */
class FeedPetUseCase(private val repository: PetRepository) {

    suspend operator fun invoke() {
        val pet = repository.current()
        repository.updateSatiation(Satiation.Full)
        repository.updateAffinity(pet.affinity + AFFINITY_REWARD)
        repository.updateAnimationState(PetAnimationState.Fed)
    }

    private companion object {
        const val AFFINITY_REWARD = 5
    }
}

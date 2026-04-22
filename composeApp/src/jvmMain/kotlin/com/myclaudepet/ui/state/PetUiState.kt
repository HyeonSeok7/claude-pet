package com.myclaudepet.ui.state

import com.myclaudepet.domain.model.AppUpdate
import com.myclaudepet.domain.model.Pet
import com.myclaudepet.domain.model.PetAnimationState
import com.myclaudepet.ui.theme.PetStrings

data class PetUiState(
    val pet: Pet,
    val speech: String? = null,
    /**
     * 지속 상태(Working/Hungry/Boring 등) 일 때 말풍선에 노출할 라벨.
     * 일회성 상태(Smile/Jumping/Touch/Fed) 는 null.
     */
    val stateLabel: String? = null,
    val permissionRequired: Boolean = false,
    val resetConfirmVisible: Boolean = false,
    val availableUpdate: AppUpdate? = null,
)

/**
 * 지속 상태 라벨 (말풍선에 계속 표시). 일회성 상태는 null.
 */
fun PetAnimationState.persistentLabel(): String? = when (this) {
    PetAnimationState.Boring -> PetStrings.LabelBoring
    PetAnimationState.Hungry -> PetStrings.LabelHungry
    PetAnimationState.WorkingPrepare -> PetStrings.LabelWorkingPrepare
    PetAnimationState.Working -> PetStrings.LabelWorking
    PetAnimationState.WorkingEnd -> PetStrings.LabelWorkingEnd
    PetAnimationState.Walking -> PetStrings.LabelWalking
    PetAnimationState.Default,
    PetAnimationState.Smile,
    PetAnimationState.Jumping,
    PetAnimationState.Touch,
    PetAnimationState.Fed -> null
}

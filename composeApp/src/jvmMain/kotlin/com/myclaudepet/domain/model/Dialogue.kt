package com.myclaudepet.domain.model

data class DialogueLine(
    val tier: DialogueTier,
    val trigger: DialogueTrigger,
    val text: String,
)

enum class DialogueTrigger {
    // 이벤트 (일회성)
    Click,
    Feed,
    LevelUp,

    // 상태 기반 (PetAnimationState 과 1:1 매칭, 상태 진입 시 자동 발동)
    Idle,            // PetAnimationState.Default 에 매핑
    Smile,
    Boring,
    Jumping,
    Touch,
    Hungry,
    Fed,
    WorkingPrepare,
    Working,
    WorkingEnd,

    // 레거시 (호환성 — 신규 사용 금지, 다음 SPEC 에서 제거 예정)
    IdleHappy,
    IdleSad,
}

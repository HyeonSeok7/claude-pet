package com.myclaudepet.domain.model

/**
 * v2 Phase 8 — 10가지 시각 상태.
 * `resourceName` 은 `/pet/{name}.png` classpath 리소스를 가리킨다.
 */
enum class PetAnimationState(val resourceName: String) {
    Default("idle_default"),
    Smile("idle_smile"),
    Boring("idle_boring"),
    Jumping("idle_jumping"),
    Touch("idle_touch"),
    WorkingPrepare("idle_working_prepare"),
    Working("idle_working"),
    WorkingEnd("idle_working_end"),
    Hungry("idle_hungry"),
    Fed("idle_fed"),

    /**
     * 걷기 애니메이션 (프레임 교차). 실제 파일은 `idle_walk_01.png`, `idle_walk_02.png`.
     * PetSprite 가 이 상태일 때 두 프레임을 300ms 간격으로 교차 렌더한다.
     */
    Walking("idle_walk"),
}

/**
 * 상태 진입 시 대응되는 대사 트리거.
 * `Default` 는 전용 `Idle` 트리거를 사용한다.
 */
val PetAnimationState.dialogueTrigger: DialogueTrigger
    get() = when (this) {
        PetAnimationState.Default -> DialogueTrigger.Idle
        PetAnimationState.Walking -> DialogueTrigger.Idle
        PetAnimationState.Smile -> DialogueTrigger.Smile
        PetAnimationState.Boring -> DialogueTrigger.Boring
        PetAnimationState.Jumping -> DialogueTrigger.Jumping
        PetAnimationState.Touch -> DialogueTrigger.Touch
        PetAnimationState.WorkingPrepare -> DialogueTrigger.WorkingPrepare
        PetAnimationState.Working -> DialogueTrigger.Working
        PetAnimationState.WorkingEnd -> DialogueTrigger.WorkingEnd
        PetAnimationState.Hungry -> DialogueTrigger.Hungry
        PetAnimationState.Fed -> DialogueTrigger.Fed
    }

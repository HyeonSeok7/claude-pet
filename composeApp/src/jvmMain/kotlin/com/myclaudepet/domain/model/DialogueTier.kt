package com.myclaudepet.domain.model

enum class DialogueTier(val minLevel: Int) {
    Formal(1),
    Friendly(6),
    Casual(11),
    Close(21),
    Intimate(51);

    companion object {
        fun forLevel(level: Int): DialogueTier =
            entries.lastOrNull { level >= it.minLevel } ?: Formal
    }
}

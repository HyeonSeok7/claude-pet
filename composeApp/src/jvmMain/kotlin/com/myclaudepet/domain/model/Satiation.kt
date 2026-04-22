package com.myclaudepet.domain.model

@JvmInline
value class Satiation(val raw: Double) : Comparable<Satiation> {

    init {
        require(raw in MIN..MAX) { "Satiation out of range: $raw" }
    }

    val percent: Int get() = raw.toInt()

    val mood: PetMood
        get() = when {
            raw < SAD_THRESHOLD -> PetMood.Sad
            raw > HAPPY_THRESHOLD -> PetMood.Happy
            else -> PetMood.Neutral
        }

    operator fun plus(delta: Double): Satiation =
        Satiation((raw + delta).coerceIn(MIN, MAX))

    operator fun minus(delta: Double): Satiation =
        Satiation((raw - delta).coerceIn(MIN, MAX))

    override fun compareTo(other: Satiation): Int = raw.compareTo(other.raw)

    companion object {
        const val MIN = 0.0
        const val MAX = 100.0
        private const val SAD_THRESHOLD = 20.0
        private const val HAPPY_THRESHOLD = 80.0

        val Full = Satiation(MAX)
    }
}

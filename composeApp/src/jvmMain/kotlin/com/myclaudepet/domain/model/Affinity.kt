package com.myclaudepet.domain.model

@JvmInline
value class Affinity(val points: Int) : Comparable<Affinity> {

    init {
        require(points in MIN..MAX) { "Affinity out of range: $points" }
    }

    val level: Int get() = points / POINTS_PER_LEVEL + 1

    val tier: DialogueTier get() = DialogueTier.forLevel(level)

    val pointsInCurrentLevel: Int get() = points % POINTS_PER_LEVEL

    val progressWithinLevel: Float
        get() = pointsInCurrentLevel / POINTS_PER_LEVEL.toFloat()

    operator fun plus(delta: Int): Affinity =
        Affinity((points + delta).coerceIn(MIN, MAX))

    override fun compareTo(other: Affinity): Int = points.compareTo(other.points)

    companion object {
        const val MIN = 0
        const val MAX = 99_999
        const val POINTS_PER_LEVEL = 100

        val Zero = Affinity(0)
    }
}

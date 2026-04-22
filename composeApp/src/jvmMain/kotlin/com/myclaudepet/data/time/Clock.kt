package com.myclaudepet.data.time

import kotlinx.datetime.Clock as KotlinClock

fun interface Clock {
    fun nowMillis(): Long
}

object SystemClock : Clock {
    override fun nowMillis(): Long = KotlinClock.System.now().toEpochMilliseconds()
}

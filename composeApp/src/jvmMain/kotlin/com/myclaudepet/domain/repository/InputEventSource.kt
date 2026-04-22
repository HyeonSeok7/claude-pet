package com.myclaudepet.domain.repository

import kotlinx.coroutines.flow.Flow

interface InputEventSource {
    val keystrokes: Flow<Unit>

    fun start(): StartResult
    fun stop()

    enum class StartResult {
        Success,
        PermissionDenied,
        Unsupported,
    }
}

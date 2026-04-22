package com.myclaudepet.data.input

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.myclaudepet.domain.repository.InputEventSource
import com.myclaudepet.domain.repository.InputEventSource.StartResult
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.logging.Level
import java.util.logging.Logger

class JNativeHookInputSource : InputEventSource, NativeKeyListener {

    private val _keystrokes = MutableSharedFlow<Unit>(
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    override val keystrokes: Flow<Unit> = _keystrokes.asSharedFlow()

    override fun start(): StartResult {
        silenceJNativeHookLogging()
        return try {
            if (!GlobalScreen.isNativeHookRegistered()) {
                GlobalScreen.registerNativeHook()
            }
            GlobalScreen.addNativeKeyListener(this)
            StartResult.Success
        } catch (e: NativeHookException) {
            if (e.code == NativeHookException.DARWIN_AXAPI_DISABLED) {
                StartResult.PermissionDenied
            } else {
                StartResult.Unsupported
            }
        } catch (e: SecurityException) {
            StartResult.PermissionDenied
        }
    }

    override fun stop() {
        runCatching {
            GlobalScreen.removeNativeKeyListener(this)
            if (GlobalScreen.isNativeHookRegistered()) {
                GlobalScreen.unregisterNativeHook()
            }
        }
    }

    override fun nativeKeyPressed(event: NativeKeyEvent) {
        _keystrokes.tryEmit(Unit)
    }

    override fun nativeKeyReleased(event: NativeKeyEvent) = Unit
    override fun nativeKeyTyped(event: NativeKeyEvent) = Unit

    private fun silenceJNativeHookLogging() {
        val logger = Logger.getLogger(GlobalScreen::class.java.getPackage().name)
        logger.level = Level.WARNING
        logger.useParentHandlers = false
    }
}

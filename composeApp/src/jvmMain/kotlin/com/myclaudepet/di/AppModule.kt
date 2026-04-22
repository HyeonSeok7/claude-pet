package com.myclaudepet.di

import com.myclaudepet.data.database.DriverFactory
import com.myclaudepet.data.database.PetDatabaseProvider
import com.myclaudepet.data.input.JNativeHookInputSource
import com.myclaudepet.data.platform.ScreenDefaults
import com.myclaudepet.data.platform.SystemPlatformBridge
import com.myclaudepet.data.repository.SqlDelightPetRepository
import com.myclaudepet.data.time.Clock
import com.myclaudepet.data.time.SystemClock
import com.myclaudepet.data.update.GitHubUpdateSource
import com.myclaudepet.db.PetDatabase
import com.myclaudepet.domain.model.PetPosition
import com.myclaudepet.domain.repository.InputEventSource
import com.myclaudepet.domain.repository.PetRepository
import com.myclaudepet.domain.repository.PlatformBridge
import com.myclaudepet.domain.repository.UpdateSource
import com.myclaudepet.domain.usecase.CheckForUpdateUseCase
import com.myclaudepet.domain.usecase.FeedOnKeystrokeUseCase
import com.myclaudepet.domain.usecase.FeedPetUseCase
import com.myclaudepet.domain.usecase.InteractWithPetUseCase
import com.myclaudepet.domain.usecase.MovePetUseCase
import com.myclaudepet.domain.usecase.ObservePetUseCase
import com.myclaudepet.domain.usecase.ResetPetUseCase
import com.myclaudepet.domain.usecase.TickSatiationUseCase
import com.myclaudepet.ui.state.PetStateHolder
import org.koin.dsl.module

val appModule = module {
    single { AppScope() }
    single<Clock> { SystemClock }
    single { DriverFactory() }
    single<PetDatabase> { PetDatabaseProvider.create(get()) }
    single<PetPosition> { ScreenDefaults.initialPosition() }
    single<PetRepository> { SqlDelightPetRepository(get(), get(), get()) }
    single<InputEventSource> { JNativeHookInputSource() }
    single<PlatformBridge> { SystemPlatformBridge() }
    single<UpdateSource> { GitHubUpdateSource() }

    factory { ObservePetUseCase(get()) }
    factory { InteractWithPetUseCase(get()) }
    factory { FeedOnKeystrokeUseCase(get()) }
    factory { TickSatiationUseCase(get()) }
    factory { MovePetUseCase(get()) }
    factory { ResetPetUseCase(get()) }
    factory { FeedPetUseCase(get()) }
    factory { CheckForUpdateUseCase(get()) }

    single {
        PetStateHolder(
            scope = get<AppScope>(),
            observePet = get(),
            interact = get(),
            feed = get(),
            tick = get(),
            move = get(),
            reset = get(),
            feedPet = get(),
            checkForUpdate = get(),
            repository = get(),
            inputSource = get(),
            platformBridge = get(),
        )
    }
}

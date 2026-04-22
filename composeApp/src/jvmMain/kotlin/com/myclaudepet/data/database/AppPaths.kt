package com.myclaudepet.data.database

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object AppPaths {
    private const val APP_NAME = "ClaudePet"

    val dataDir: Path by lazy {
        val os = System.getProperty("os.name").lowercase()
        val home = System.getProperty("user.home")
        val base = when {
            os.contains("mac") -> Paths.get(home, "Library", "Application Support", APP_NAME)
            os.contains("win") -> {
                val localAppData = System.getenv("LOCALAPPDATA") ?: "$home\\AppData\\Local"
                Paths.get(localAppData, APP_NAME)
            }
            else -> Paths.get(home, ".local", "share", APP_NAME.lowercase())
        }
        Files.createDirectories(base)
        base
    }

    val databaseFile: Path get() = dataDir.resolve("pet.db")
}

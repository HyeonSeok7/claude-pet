package com.myclaudepet.data.update

/**
 * 빌드 시 주입된 classpath 리소스 (app_version.txt / github_owner.txt / github_repo.txt) 를 읽는다.
 * 파일이 없으면 기본값(폴백) 반환.
 */
object AppMeta {
    val version: String by lazy { read("app_version.txt") ?: "0.0.0" }
    val githubOwner: String by lazy { read("github_owner.txt") ?: "YOUR_GITHUB_USERNAME" }
    val githubRepo: String by lazy { read("github_repo.txt") ?: "claude-pet" }

    private fun read(name: String): String? = runCatching {
        AppMeta::class.java.classLoader.getResourceAsStream(name)
            ?.use { it.readBytes().toString(Charsets.UTF_8).trim() }
    }.getOrNull()?.takeIf { it.isNotBlank() }
}

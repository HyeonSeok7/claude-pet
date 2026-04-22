package com.myclaudepet.data.update

import com.myclaudepet.domain.model.AppUpdate
import com.myclaudepet.domain.model.isOlderThan
import com.myclaudepet.domain.repository.UpdateSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

/**
 * GitHub Releases "latest" 엔드포인트 (`/repos/:owner/:repo/releases/latest`) 를 조회.
 * 네트워크 오류/404/파싱 실패 시 null 반환 — 앱 시작을 방해하지 않는다.
 */
class GitHubUpdateSource(
    private val owner: String = AppMeta.githubOwner,
    private val repo: String = AppMeta.githubRepo,
    private val currentVersion: String = AppMeta.version,
    private val timeoutMs: Int = 5_000,
) : UpdateSource {

    override suspend fun latest(): AppUpdate? = withContext(Dispatchers.IO) {
        if (owner == "YOUR_GITHUB_USERNAME") return@withContext null
        val url = URL("https://api.github.com/repos/$owner/$repo/releases/latest")
        val conn = runCatching { url.openConnection() as HttpURLConnection }.getOrNull()
            ?: return@withContext null
        conn.connectTimeout = timeoutMs
        conn.readTimeout = timeoutMs
        conn.setRequestProperty("Accept", "application/vnd.github+json")
        conn.setRequestProperty("User-Agent", "ClaudePet-UpdateChecker")

        val body = runCatching {
            conn.inputStream.use { it.readBytes().toString(Charsets.UTF_8) }
        }.getOrNull()
        conn.disconnect()
        body ?: return@withContext null

        val parsed = runCatching { json.decodeFromString<ReleasePayload>(body) }.getOrNull()
            ?: return@withContext null
        val latest = parsed.tagName.orEmpty().takeIf { it.isNotBlank() } ?: return@withContext null

        if (!currentVersion.isOlderThan(latest)) return@withContext null
        AppUpdate(
            currentVersion = currentVersion,
            latestVersion = latest,
            releaseUrl = parsed.htmlUrl.orEmpty(),
            notes = parsed.body.orEmpty(),
        )
    }

    @Serializable
    private data class ReleasePayload(
        val tag_name: String? = null,
        val html_url: String? = null,
        val body: String? = null,
    ) {
        val tagName get() = tag_name
        val htmlUrl get() = html_url
    }

    private companion object {
        val json = Json { ignoreUnknownKeys = true }
    }
}

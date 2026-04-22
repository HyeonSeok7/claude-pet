package com.myclaudepet.domain.model

/**
 * GitHub Releases 에서 감지한 새 버전.
 * `currentVersion < latestVersion` 일 때만 생성된다.
 */
data class AppUpdate(
    val currentVersion: String,
    val latestVersion: String,
    val releaseUrl: String,
    val notes: String,
)

/**
 * SemVer 문자열 비교. "v" 접두사 허용. 파싱 실패 시 false.
 * @return [other] 가 [this] 보다 크면 true.
 */
fun String.isOlderThan(other: String): Boolean {
    val a = toVersionTuple() ?: return false
    val b = other.toVersionTuple() ?: return false
    for (i in 0 until maxOf(a.size, b.size)) {
        val av = a.getOrElse(i) { 0 }
        val bv = b.getOrElse(i) { 0 }
        if (av != bv) return av < bv
    }
    return false
}

private fun String.toVersionTuple(): List<Int>? = runCatching {
    removePrefix("v").split(".").map { it.takeWhile(Char::isDigit).toInt() }
}.getOrNull()

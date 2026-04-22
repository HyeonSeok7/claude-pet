package com.myclaudepet.domain.repository

import com.myclaudepet.domain.model.AppUpdate

/**
 * 원격 업데이트 정보 조회. GitHub Releases API 등 구현체별 소스.
 */
fun interface UpdateSource {
    /**
     * 새 버전이 있으면 [AppUpdate] 반환, 없거나 네트워크 실패면 null.
     */
    suspend fun latest(): AppUpdate?
}

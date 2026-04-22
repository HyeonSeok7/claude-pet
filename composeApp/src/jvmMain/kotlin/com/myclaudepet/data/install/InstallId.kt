package com.myclaudepet.data.install

/**
 * 배포 빌드의 classpath 에 주입되는 `install_id.txt` 를 읽는다.
 *
 * - `./gradlew packageDmg` 등 배포 태스크는 `build.gradle.kts` 의
 *   `generateInstallId` 태스크가 선행되어 UUID 파일이 resources 로 포함된다.
 * - `./gradlew run` 개발 태스크는 해당 파일이 생성되지 않으므로 null 이 반환되고,
 *   데이터 리셋 로직이 건너뛰어진다 (개발 편의 유지).
 *
 * 배포 앱 기준: 새 .dmg/.msi 설치마다 UUID 가 달라져 DB 저장값과 불일치하면
 * `SqlDelightPetRepository` 에서 진행 데이터를 초기화한다.
 */
object InstallId {
    fun bundled(): String? {
        // 개발 `./gradlew run` 은 `claudepet.dev=true` 로 실행되어 install_id 가
        // classpath 에 있어도 리셋 로직을 건너뛰게 한다.
        if (System.getProperty("claudepet.dev") == "true") return null
        return runCatching {
            val stream = InstallId::class.java.classLoader
                .getResourceAsStream("install_id.txt")
                ?: return@runCatching null
            stream.use { it.readBytes().toString(Charsets.UTF_8).trim() }
        }.getOrNull()?.takeIf { it.isNotBlank() }
    }
}

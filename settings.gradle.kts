plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "PixelDuel"
include(":common")
include(":desktop")

val enginePath = File("B:\\Java\\Kotlin\\Engine2D")

if (enginePath.exists()) {
    include(":engine")
    project(":engine").projectDir = enginePath
} else println("找不到本地引擎路径")
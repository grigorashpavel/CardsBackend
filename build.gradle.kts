val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_version: String by project
val koin_ksp_version: String by project
val bcrypt_version: String by project
val exposed_version: String by project
val h2_version: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.9"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
    id("com.google.devtools.ksp") version "1.9.23-1.0.20"
}

group = "com.pasha"
version = "0.0.1"

application {
    mainClass.set("com.pasha.ApplicationKt")

    sourceSets.main {
        java.srcDirs("build/generated/ksp/main/kotlin")
    }

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}


dependencies {
    // Koin
    implementation("io.insert-koin:koin-ktor:$koin_version") // Koin for Ktor
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version") // Koin Logger
    implementation("io.insert-koin:koin-annotations:$koin_ksp_version") // Koin Annotations for KSP
    ksp("io.insert-koin:koin-ksp-compiler:$koin_ksp_version") // Koin KSP Compiler for KSP

    // BCrypt
    implementation("com.ToxicBakery.library.bcrypt:bcrypt:$bcrypt_version")

    // Status Pages
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")

    // Exposed & H2
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("com.h2database:h2:$h2_version")

    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-cio-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

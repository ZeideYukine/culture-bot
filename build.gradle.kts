import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.serialization") version "1.5.10"

    id("com.github.ben-manes.versions") version "0.39.0"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.zeide"
version = "1.0-ALPHA"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")

    implementation("io.ktor:ktor-client-core:1.6.0")
    implementation("io.ktor:ktor-client-serialization:1.6.0")

    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.14.1")
    implementation("io.github.microutils:kotlin-logging:2.0.8")

    implementation("dev.kord:kord-core:0.7.x-SNAPSHOT")
    implementation("dev.kord.x:emoji:0.5.0-SNAPSHOT")

    implementation("de.androidpit:color-thief:1.1.2")
}

tasks.withType<Jar> {
    manifest {
        attributes(mapOf(
            "Main-Class" to "com.zeide.culturebot.LaunchKt",
            "Multi-Release" to true
        ))
    }
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}

tasks.create("stage") {
    dependsOn("shadowJar")
}
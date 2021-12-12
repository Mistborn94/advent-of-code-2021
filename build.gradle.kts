import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
}

group = "dev.renette"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    testImplementation(kotlin("test-junit5"))
}

tasks.withType<Test> {
    useJUnitPlatform()
    dependsOn("cleanTest")
    testLogging.showStandardStreams = true
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
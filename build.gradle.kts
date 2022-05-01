import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "me.tropicalshadow"
version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies{
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    implementation("org.reflections:reflections:0.10.2")

}
tasks {
    withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
        archiveFileName.set(rootProject.name + "-V" + project.version + ".jar")
        relocate("kotlin", "com.github.tropicalshadow.actionreward.dependencies.kotlin")
        relocate("org.reflections", "com.github.tropicalshadow.actionreward.dependencies.reflections")
        exclude("DebugProbesKt.bin")
        exclude("META-INF/**")
    }

    processResources {
        filter<org.apache.tools.ant.filters.ReplaceTokens>("tokens" to mapOf("version" to project.version))
    }

}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
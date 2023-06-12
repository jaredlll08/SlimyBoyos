pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        maven("https://repo.spongepowered.org/repository/maven-public/") {
            name = "Sponge Snapshots"
        }
        maven("https://maven.minecraftforge.net") {
            name = "Forge"
        }
        maven("https://maven.blamejared.com") {
            name = "BlameJared"
        }
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "SlimyBoyos"
include("common", "fabric", "forge")
import com.blamejared.modtemplate.Utils
plugins {
    java
    `maven-publish`
    id("org.spongepowered.gradle.vanilla") version "0.2.1-SNAPSHOT"
    id("com.blamejared.modtemplate")
}

val minecraftVersion: String by project
val commonRunsEnabled: String by project
val commonClientRunName: String? by project
val commonServerRunName: String? by project
val modName: String by project
val modId: String by project
val modVersion: String by project

val baseArchiveName = "${modName}-common-${minecraftVersion}"

version = Utils.updatingVersion(modVersion)
base {
    archivesName.set(baseArchiveName)
}

minecraft {
    version(minecraftVersion)
}

dependencies {
    compileOnly("org.spongepowered:mixin:0.8.5")
}

tasks.processResources {

    val buildProps = project.properties

    filesMatching("pack.mcmeta") {

        expand(buildProps)
    }
}
publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            artifactId = baseArchiveName
            from(components["java"])
        }
    }

    repositories {
        maven("file://${System.getenv("local_maven")}")
    }
}
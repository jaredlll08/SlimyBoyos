import com.blamejared.slimyboyos.gradle.Versions

plugins {
    id("org.spongepowered.gradle.vanilla") version "0.2.1-SNAPSHOT"
    id("com.blamejared.slimyboyos.default")
}

minecraft {
    version(Versions.MINECRAFT)
    runs {
        client("Common Client") {
            workingDirectory(project.file("run"))
        }
    }
}

dependencies {
    compileOnly("org.spongepowered:mixin:0.8.5")
}
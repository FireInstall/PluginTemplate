//import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    `java-library`
    //java
    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("xyz.jpenilla.run-paper") version "2.3.0" // Adds runServer and runMojangMappedServer tasks for testing
    id("xyz.jpenilla.run-waterfall") version "2.3.0" // Adds runWaterfallServer task for testing
}

group = "de.greensurvivors"
version = "1.0.0-SNAPSHOT"
description = "A Greensurvivors Plugin "
// this is the minecraft major version.
val mainMCVersion by extra("1.21")
// this is the mc major and subversion like 1.21.1
val mcVersion by extra("$mainMCVersion")

// we only work with paper and downstream!
paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

java {
    // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenLocal()

    //paper
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    paperweight.paperDevBundle("$mcVersion-R0.1-SNAPSHOT")
    // Waterfall
    compileOnly("io.github.waterfallmc:waterfall-api:$mainMCVersion-R0.1-SNAPSHOT")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything

        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release.set(21)
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything

        filesMatching(listOf("**/plugin.yml", "**/bungee.yml")) {
            expand(project.properties)
        }
    }

    runWaterfall {
        waterfallVersion(mainMCVersion)
    }
}
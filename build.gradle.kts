plugins {
    kotlin("jvm") version "1.6.10"
}

group = "io.github.changwook987"
version = "1.0-SNAPSHOT"

project.extra.set("packageName", name.replace("-", ""))
project.extra.set("pluginName", name.split("-").joinToString("") { it.capitalize() })

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.github.monun:kommand-api:2.8.0")
    implementation("io.github.monun:invfx-api:3.0.1")
    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    processResources {
        filesMatching("**/*.yml") {
            expand(project.properties)
            expand(project.extra.properties)
        }
    }

    create<Jar>("paper") {
        from(sourceSets["main"].output)
        archiveBaseName.set(project.extra.properties["pluginName"].toString())
        archiveVersion.set("")

        doLast {
            copy {
                from(archiveFile)
                into(File(rootDir, "server/plugins"))
            }
        }
    }
}
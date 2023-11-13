plugins {
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")

    implementation(project(":api"))
}

val fileName = "InventoryAPI.jar"

tasks {
    jar {
        finalizedBy(shadowJar)
        archiveFileName.set(fileName)
    }
    shadowJar {
        archiveFileName.set(fileName)
    }
}
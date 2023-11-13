dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    compileOnly(project(":api"))
}

tasks {
    jar {
        archiveFileName.set("InventoryAPI-Example.jar")
    }
}
plugins {
    id("java")
}

val targetJavaVersion = 17

allprojects {
    apply(plugin = "java")

    group = "kor.toxicity.inventory"
    version = "1.0.4"

    repositories {
        mavenCentral()
        maven(url = "https://repo.papermc.io/repository/maven-public/")
    }

    dependencies {
        testImplementation(platform("org.junit:junit-bom:5.9.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")

        compileOnly("org.projectlombok:lombok:1.18.30")
        annotationProcessor("org.projectlombok:lombok:1.18.30")

        testCompileOnly("org.projectlombok:lombok:1.18.30")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
    }
    tasks {
        compileJava {
            options.encoding = Charsets.UTF_8.name()
            options.release.set(targetJavaVersion)
        }
        test {
            useJUnitPlatform()
        }
        processResources {
            val props = mapOf(
                "version" to version.toString(),
            )
            filteringCharset = Charsets.UTF_8.name()
            inputs.properties(props)
            filesMatching("plugin.yml") {
                expand(props)
            }
        }
    }
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
}
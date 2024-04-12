plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
    alias(libs.plugins.runpaper)
}

group = "de.sotterbeck"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    testImplementation(libs.papermc)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.mockbukkit)
    testImplementation(libs.assertk)

    compileOnly(libs.papermc)
    implementation(libs.cloud.paper)
    implementation(libs.cloud.kotlin.annotations)
    implementation(libs.cloud.kotlin.extensions)

    shadow(libs.kotlin.std)
}

kotlin {
    jvmToolchain(17)
}

tasks {
    processResources {
        filesMatching("paper-plugin.yml") {
            expand(project.properties)
        }
    }

    test {
        useJUnitPlatform()
    }

    runServer {
        minecraftVersion("1.20.4")
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
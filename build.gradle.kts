plugins {
    kotlin("jvm") version "2.1.10"
    `maven-publish`
}

// 1. Correct JitPack Group and Next Version
group = "com.github.DivyansPathak"
version = "1.0.4"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-client-core:2.3.8")
    implementation("io.ktor:ktor-client-cio:2.3.8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

// 2. Use JDK 19 locally, but compile to Java 17 bytecode for maximum compatibility
kotlin {
    jvmToolchain(19)
}

tasks.withType<JavaCompile> {
    options.release.set(17)
}

// 3. Explicit Maven Publication Block for JitPack
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            groupId = "com.github.DivyansPathak"
            artifactId = "ytmusic-engine-kotlin"
            version = "1.0.4"
        }
    }
}
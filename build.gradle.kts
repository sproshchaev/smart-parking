plugins {
    // kotlin("jvm") version "1.9.23"
    kotlin("jvm") version "2.3.20"
}

group = "com.prosoft"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:6.1.3")
    testImplementation("org.junit.jupiter:junit-jupiter-params:6.1.3")
    testImplementation("io.mockk:mockk:1.14.11")

//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
//    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
//    testImplementation("io.mockk:mockk:1.13.10")


}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}
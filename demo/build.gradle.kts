plugins {
    java
    application
}

group = "net.flamgop"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.jetbrains.annotations)
    implementation(rootProject)
}
plugins {
    id("java-library")
    id("maven-publish")
    id("me.champeau.jmh") version "0.7.3"
}

group = "net.flamgop"
version = "1.0-SNAPSHOT"

jmh {
//    jvmArgs.add("-XX:+UnlockDiagnosticVMOptions")
//    jvmArgs.add("-XX:+PrintAssembly")
//    jvmArgs.add("-XX:PrintAssemblyOptions=intel")
//    jvmArgs.add("-XX:CompileCommand=print,net/flamgop/NoiseBenchmark.testSampling")
    jvmArgs.add("-p noiseType=GABOR")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnlyApi(libs.jetbrains.annotations)

    testImplementation(platform(tests.junit.bom))
    testImplementation(tests.junit.jupiter)
    testImplementation(tests.apache.commons.math)
    testRuntimeOnly(tests.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
}


java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "net.flamgop.lattice"
            artifactId = "lattice"
            version = "0.0.1"
        }
    }
}
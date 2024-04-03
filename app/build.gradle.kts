import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    application
    jacoco
    id("io.freefair.lombok") version "8.3"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("checkstyle")
}

application {
    mainClass.set("hexlet.code.App");
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.h2database:h2:2.2.222")
    implementation("org.postgresql:postgresql:42.1.4")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.apache.commons:commons-text:1.10.0")
    implementation("gg.jte:jte:3.1.0")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    implementation("io.javalin:javalin:6.1.3")
    implementation("io.javalin:javalin-bundle:6.1.3")
    implementation("io.javalin:javalin-rendering:6.1.3")
    implementation("com.mashape.unirest:unirest-java:1.3.1")
    implementation("org.jsoup:jsoup:1.17.2")


    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.squareup.okhttp3:mockwebserver:5.0.0-alpha.12")
}

tasks.compileJava {
    options.release = 20
}

tasks.test {
    useJUnitPlatform()
    // https://technology.lastminute.com/junit5-kotlin-and-gradle-dsl/
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        showStackTraces = true
        showCauses = true
        showStandardStreams = true
    }
    finalizedBy(tasks.jacocoTestReport)
}

jacoco {
    toolVersion = "0.8.9"
    reportsDirectory
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
    }

}
plugins {
    kotlin("jvm") version "1.7.21"
    kotlin("plugin.serialization") version "1.7.21"
    id("org.jetbrains.kotlin.kapt") version "1.7.21"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.7.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.micronaut.application") version "3.6.3"
}

version = "1.1.3"
group = "kiinse.programs.metricswindapi"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    kapt("io.micronaut:micronaut-http-validation")
    runtimeOnly("ch.qos.logback:logback-classic")

    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("io.micronaut:micronaut-validation")
    runtimeOnly("ch.qos.logback:logback-classic")

    implementation("org.mongodb:mongo-java-driver:3.12.11")
    implementation("com.auth0:java-jwt:4.2.1")
    implementation("org.json:json:20220924")
    implementation("org.slf4j:slf4j-api:2.0.5")
    implementation("org.slf4j:slf4j-log4j12:2.0.5")
    implementation("org.apache.logging.log4j:log4j-core:2.19.0")
    implementation("org.apache.logging.log4j:log4j-api:2.19.0")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation(project(":api-metricswind"))
    implementation(files("libs/api-core-1.0.1.jar"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

application {
    mainClass.set("kiinse.programs.metricswindapi.MetricsWindAPIKt")
}

kapt {
    useBuildCache = false
}

graalvmNative.toolchainDetection.set(false)
micronaut {
    runtime("jetty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("kiinse.programs.metricswindapi.*")
    }
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}



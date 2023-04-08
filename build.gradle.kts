import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.plugin.SpringBootPlugin

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.7.22"))
        classpath("com.expediagroup:graphql-kotlin-gradle-plugin:4.0.0-alpha.12")
        classpath("org.junit.platform:junit-platform-gradle-plugin:1.2.0")
    }
}

group = "com.dongholab"
version = "0.0.1-SNAPSHOT"

plugins {
    val kotlinVersion = "1.7.22"
    id("com.netflix.dgs.codegen") version "5.6.3" apply false
    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion apply false
    kotlin("plugin.spring") version kotlinVersion apply false
    kotlin("plugin.jpa") version kotlinVersion apply false
    id("org.springframework.boot") version "3.0.4" apply false
    id("io.spring.dependency-management") version "1.1.0"
    id("com.avast.gradle.docker-compose") version "0.13.3"
    id("org.flywaydb.flyway") version "7.8.2" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0" apply false
    id("org.jlleitschuh.gradle.ktlint-idea") version "11.0.0"
    idea
    groovy
    jacoco
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    repositories {
        mavenCentral()
    }

    ktlint {
        disabledRules.apply {
            add("import-ordering")
            add("no-wildcard-imports")
        }
        filter {
            exclude("*.kts")
            exclude("**/generated/**")
        }
    }
}

tasks {
    register<Exec>("lint") {
        commandLine = "./gradlew ktlintCheck".split(" ")
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "jacoco")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.junit.platform.gradle.plugin")

    dependencyManagement {
        val springCloudVersion = "2022.0.1"
        imports {
            mavenBom(SpringBootPlugin.BOM_COORDINATES)
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}")
        }
        dependencies {
            dependencySet("com.graphql-java:16.2") {
                entry("graphql-java")
            }
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    ext {
        set("logbackVersion", "1.4.5")
        set("logbackJsonVersion", "0.1.5")
        set("logstashLogbackVersion", "6.4")
        set("jacksonVersion", "2.14.1")
    }

    val logbackVersion: String? by ext
    val logbackJsonVersion: String? by ext
    val logstashLogbackVersion: String? by ext
    val jacksonVersion: String? by ext

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
        implementation(kotlin("reflect"))

        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
        implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
        implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
        implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

        implementation("org.apache.poi:poi-ooxml:3.12")

        implementation(platform("com.amazonaws:aws-java-sdk-bom:1.11.1034"))
        implementation("software.amazon.awssdk:sso:2.17.6")

        implementation("ch.qos.logback:logback-core:${logbackVersion}")
        implementation("ch.qos.logback:logback-classic:${logbackVersion}")
        implementation("ch.qos.logback:logback-access:${logbackVersion}")
        implementation("ch.qos.logback.contrib:logback-jackson:${logbackJsonVersion}")
        implementation("net.logstash.logback:logstash-logback-encoder:${logstashLogbackVersion}")
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("org.springframework.boot:spring-boot-starter-aop")
        implementation("com.coreoz:windmill:1.2.2")

        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
            exclude(module = "mockito-core")
        }

        testRuntimeOnly("org.junit.platform:junit-platform-commons:1.9.2")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
        testImplementation("io.mockk:mockk:1.13.4")
        testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
        testImplementation("org.mockito:mockito-inline:5.2.0")
        testImplementation("com.ninja-squad:springmockk:4.0.2")
        testImplementation("io.kotest:kotest-runner-junit5:5.5.5")
        testImplementation("io.kotest:kotest-assertions-core:5.5.5")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform {
            includeEngines("junit-jupiter")
            excludeEngines("junit-vintage")
        }
        testLogging {
            showStackTraces = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }

    tasks.test {
        extensions.configure(JacocoTaskExtension::class) {
            isEnabled = true
            output = JacocoTaskExtension.Output.FILE
        }
        finalizedBy("jacocoTestReport")
    }

    tasks.jacocoTestReport {
        reports {
            // 원하는 리포트를 켜고 끌 수 있다.
            html.isEnabled = true
            xml.isEnabled = false
            csv.isEnabled = false
        }
        finalizedBy("jacocoTestCoverageVerification")
    }

    tasks.jacocoTestCoverageVerification {
        violationRules {
            rule {
                limit {
                    minimum = "0.30".toBigDecimal()
                }
            }
            rule {
                enabled = true
                element = "CLASS"
                limit {
                    counter = "BRANCH"
                    value = "COVEREDRATIO"
                    minimum = "0.90".toBigDecimal()
                }
            }
        }
    }

    val testCoverage by tasks.registering {
        group = "verification"
        description = "Runs the unit tests with coverage"
        dependsOn(
            ":test",
            ":jacocoTestReport",
            ":jacocoTestCoverageVerification"
        )
        tasks["jacocoTestReport"].mustRunAfter(tasks["test"])
        tasks["jacocoTestCoverageVerification"].mustRunAfter(tasks["jacocoTestReport"])
    }

}

dockerCompose {
    useComposeFiles = listOf("./tools/docker-compose.yml")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

repositories {
    mavenCentral()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "17"
}

import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true

apply(plugin = "groovy")

plugins {
    kotlin("kapt")
    kotlin("plugin.jpa")
    `java-test-fixtures`
    id("org.flywaydb.flyway")
    id("kotlin-allopen")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

sourceSets {
    create("integrationTest") {
        withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
            kotlin.srcDir("src/integrationTest/kotlin")
            resources.srcDir("src/integrationTest/resources")
            compileClasspath += sourceSets["test"].runtimeClasspath
            runtimeClasspath += sourceSets["test"].runtimeClasspath
        }
    }
}

val integrationTest = task<Test>("integrationTest") {
    description = "Runs the integration tests"
    group = "verification"
    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
}

val unitTest = task<Test>("unitTest") {
    description = "Runs the unit tests"
    group = "verification"
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
}

tasks.test {
    dependsOn(unitTest)
    dependsOn(integrationTest)
}

val sentryVersion: String? by ext
val queryDslVersion: String = "5.0.0"

dependencies {
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("org.springframework.boot:spring-boot-starter-jdbc")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.retry:spring-retry:2.0.0")
    implementation("com.graphql-java:graphql-java:16.2")
    implementation("com.google.code.gson:gson:2.8.7")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")

    implementation("org.springframework.kafka:spring-kafka:3.0.5")
    implementation("org.springframework.data:spring-data-envers")
    implementation("org.apache.commons:commons-text:1.9")
    implementation("org.apache.commons:commons-csv:1.9.0")
    implementation("org.jsoup:jsoup:1.12.1")
    implementation(kotlin("stdlib"))
    implementation(kotlin("script-runtime"))
    implementation("org.jsoup:jsoup:1.11.3")
    testImplementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.8.0")
    runtimeOnly("org.postgresql:postgresql:42.2.20")
    runtimeOnly("mysql:mysql-connector-java:8.0.32")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    implementation("com.querydsl:querydsl-jpa:$queryDslVersion:jakarta")
    implementation("com.querydsl:querydsl-core:$queryDslVersion")
    kapt("com.querydsl:querydsl-apt:$queryDslVersion:jakarta")
    kapt("com.querydsl:querydsl-kotlin-codegen:$queryDslVersion")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

flyway {
    url = "jdbc:mysql://localhost:3306/dongholab?useUnicode=yes&characterEncoding=UTF-8&serverTimezone=UTC"
    user = "dongholab"
    password = "root"
}

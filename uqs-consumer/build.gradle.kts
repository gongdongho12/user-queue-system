import org.springframework.boot.gradle.tasks.bundling.BootJar

tasks {
    withType<BootJar> {
        mainClass.set("com.dongholab.uqs.consumer.UqsConsumerApplicationKt")
    }
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
    shouldRunAfter("test")
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

dependencies {
    api(project(":uqs-domain"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.amazonaws:aws-java-sdk-sts")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2")
    implementation("org.springframework.kafka:spring-kafka:3.0.5")
    implementation("io.projectreactor:reactor-core:3.4.2")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.1")
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.0.5")
    implementation("org.springframework.retry:spring-retry:2.0.0")

    implementation("net.javacrumbs.shedlock:shedlock-spring:4.24.0")
    implementation("net.javacrumbs.shedlock:shedlock-provider-jdbc-template:4.24.0")

    testImplementation(testFixtures(project(":uqs-domain")))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

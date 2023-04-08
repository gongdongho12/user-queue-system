import org.springframework.boot.gradle.tasks.bundling.BootJar

tasks {
    withType<BootJar> {
        mainClass.set("com.dongholab.uqs.batch.UqsBatchApplicationKt")
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
    useJUnitPlatform {
        excludeTags("batchTest")
    }
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

val logstashLogbackVersion: String? by ext
val sentryVersion: String? by ext

dependencies {
    implementation(project(":uqs-domain"))
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.batch:spring-batch-integration:4.2.7.RELEASE")
    implementation("com.google.code.gson:gson:2.8.7")
    implementation("net.logstash.logback:logstash-logback-encoder:${logstashLogbackVersion}")

    testImplementation(testFixtures(project(":uqs-domain")))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.batch:spring-batch-test")
}

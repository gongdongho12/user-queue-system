import com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("com.netflix.dgs.codegen")
}

tasks {
    withType<BootJar> {
        mainClass.set("com.dongholab.uqs.api.UqsApiApplicationKt")
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
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    compileOnly("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("com.amazonaws:aws-java-sdk-sts")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2")
    implementation("com.graphql-java:graphql-java:19.2")
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter:6.0.1")
    implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies"))
    implementation("io.projectreactor:reactor-core:3.5.3")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.1.7")
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.0.4")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4")
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars")
    testImplementation(testFixtures(project(":uqs-domain")))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<GenerateJavaTask> {
    schemaPaths = mutableListOf("${projectDir}/src/main/resources/dongholab.graphqls")
    packageName = "com.dongholab.uqs.api.generated"
    generateDataTypes = true
    snakeCaseConstantNames = true
    language = "kotlin"
    generateKotlinNullableClasses = false
    typeMapping = mutableMapOf(
        "CrTimestamp" to "java.time.OffsetDateTime"
    )
}

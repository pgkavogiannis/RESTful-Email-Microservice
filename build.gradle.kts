import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootWar

val buildProfile: String? by project
val defaultProfileIfAbsent = buildProfile ?: "dev"

group = "com.email.microservice"
version = "0.0.1-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_1_8

plugins {

    val kotlinVersion = "1.6.21"
    val palantirDockerVersion = "0.33.0"

    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.springdoc.openapi-gradle-plugin") version "1.3.4"

    war

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion

    id("com.palantir.docker") version palantirDockerVersion
    id("com.palantir.docker-compose") version palantirDockerVersion
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {

    /**
     * Springboot
     */
    val openApiVersion = "1.6.7"

    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-webflux")
    implementation("org.springdoc:springdoc-openapi-ui:$openApiVersion")
    implementation("org.springdoc:springdoc-openapi-kotlin:$openApiVersion")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:$openApiVersion")

    /**
     * Kotlin
     */
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")

}

openApi {
    apiDocsUrl.set("http://localhost:7000/v3/api-docs")
    outputDir.set(file("$buildDir/docs"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<BootWar>().configureEach {
    archiveFileName.set("${rootProject.name}.war")
    launchScript()
}

/**
 * This task deletes the build folder, copies content of properties file matches the given build profile
 * and saves it to application.properties in src directory as well as in build directory.
 * Finally, it builds the project
 */
tasks.register("buildWithProfile") {
    doFirst {
        delete(buildDir)
    }
    doLast {
        copy {
            val projectResources = layout.projectDirectory.dir("src/main/resources")

            from(projectResources) {
                include("*-${defaultProfileIfAbsent}.*")
            }

            into(layout.buildDirectory.dir("resources/main"))
            into(projectResources)

            rename("(.+)-${defaultProfileIfAbsent}(.+)", "$1$2")

        }
    }
    finalizedBy(tasks.build)
}

/**
 * Execute the SpringBoot application in given profile and with debugger listening on port 7001
 */
tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    args("--spring.profiles.active=${defaultProfileIfAbsent}")
    jvmArgs = listOf("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=7001")
}

tasks.register("bootRunDev") {
    group = "application"
    description = "Runs this project as a Spring Boot application with the dev profile"
    doFirst {
        tasks.bootRun.configure {
            systemProperty("spring.profiles.active", "dev")
        }
    }
    finalizedBy("bootRun")
}

/**
 * Dockerize the application using plugin
 */

configure<com.palantir.gradle.docker.DockerExtension> {
    dependsOn(tasks.getByName<Copy>("dockerPrepare"))

    name = "registry.gitlab.com/public-programming-projects/email-microservice"
    setDockerfile(File("./Dockerfile.plugin"))

    val bootWar = tasks.getByName<BootWar>("bootWar")
    files(bootWar.archiveFile)
    buildArgs(mapOf("JAR_FILE" to bootWar.archiveFileName.get()))

    noCache(true)
}

dockerCompose {
    setDockerComposeFile(File("docker-compose.plugin.yml"))
}
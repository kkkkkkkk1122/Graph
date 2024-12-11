plugins {
    id("java")
    id ("application")
}

group = "org.example"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(files("libs/log4j-api-2.24.2.jar"))
    implementation(files("libs/log4j-core-2.24.2.jar"))
}

application {
    mainClass = "org.example.Main"
}


tasks.jar {
    manifest {
        attributes(
            "Main-Class" to application.mainClass.get()
        )
    }
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
tasks.test {
    useJUnitPlatform()
}
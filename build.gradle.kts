plugins {
    id("java")
}

group = "ru.qngdjas"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "windows-1251"
}

tasks.withType<ProcessResources> {
    filteringCharset = "windows-1251"
}
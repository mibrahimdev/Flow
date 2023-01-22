// Top-level build file where you can add configuration options common to all sub-projects/modules.
allprojects {
    ext {
        set("compose_version", "1.3.3")
    }
}
buildscript {

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}
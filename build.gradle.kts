// Top-level build file where you can add configuration options common to all sub-projects/modules.
allprojects {
    ext {
        set("compose_version", "1.0.0")
    }
}
buildscript {

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath(kotlin("gradle-plugin", version = "1.5.10"))

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
allprojects {
    ext {
        set("compose_version", "1.3.3")
    }

    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf(
                // Avoid having to stutter experimental annotations all over the codebase
                "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
                "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
                "-opt-in=androidx.compose.runtime.ExperimentalComposeApi",
                "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
                "-opt-in=com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi",
                "-opt-in=com.google.accompanist.pager.ExperimentalPagerApi",
                "-opt-in=com.google.accompanist.permissions.ExperimentalPermissionsApi",
                "-opt-in=kotlin.ExperimentalUnsignedTypes",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.InternalCoroutinesApi"
            )
        }
    }
}
buildscript {

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}
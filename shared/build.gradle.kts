import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    jvm()
    
    androidLibrary {
       namespace = "com.djx.kmpappsubstrate.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            // icon
            implementation(libs.material.icons.extended)
            // webview
            api("io.github.kevinnzou:compose-webview-multiplatform:2.0.3")
            // navigation
            implementation(libs.navigation.compose)
            // embedded static server for the bundled web application
            implementation(libs.ktor.server.core)
            implementation(libs.ktor.server.cio)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(libs.logback)
        }
    }
}

val prepareWebComposeResources by tasks.registering(Sync::class) {
    from(layout.projectDirectory.dir("src/commonMain/resources/assets"))
    into(layout.buildDirectory.dir("generatedWebComposeResources/files/web"))
}

compose.resources {
    customDirectory(
        sourceSetName = "commonMain",
        directoryProvider = prepareWebComposeResources.map {
            layout.buildDirectory.dir("generatedWebComposeResources").get()
        },
    )
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}


// static resources
tasks.register("genComposeRes") {
    group = "compose resources"
    description = "Generate Compose Multiplatform Res class"

    dependsOn("generateComposeResClass")
}

// Copy Static
tasks.register<Copy>("copyWebDistToIOS") {
    from("${project.projectDir}/src/commonMain/resources/www")
    into("${buildDir}/iosResources/www")
}

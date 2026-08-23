import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    android {
        // Distinto del namespace de androidApp (es.elchecf.app): AGP no permite que
        // una app y la librería de la que depende compartan namespace/paquete de R.
        namespace = "es.elchecf.app.shared"
        compileSdk = 37
        minSdk = 26

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }

        androidResources {
            enable = true
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Alias compose.* (accessor del plugin org.jetbrains.compose): deprecado desde CMP 1.10
            // a favor de coordenadas Maven directas, pero las coordenadas directas
            // (org.jetbrains.compose.material3:material3, etc.) no se resuelven en Maven Central
            // con la versión 1.11.1 — pendiente de revisar cuando JetBrains publique la ruta de
            // migración real. El accessor sigue funcionando, solo emite warning.
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }
        androidMain.dependencies {
            // FASE 1: sin dependencias Android-only propias todavía (koin-android vive en androidApp)
        }
        iosMain.dependencies {
            // FASE 1 (iOS): sin dependencias adicionales todavía; Ktor Darwin llega en Fase 8
        }
    }

    jvmToolchain(17)
}

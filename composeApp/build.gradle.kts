import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ktlint)
}

ktlint {
    // function-naming (@Composable en PascalCase, factory de UIViewController) se
    // desactiva vía .editorconfig (ktlint_standard_function-naming = disabled), no aquí.

    // Excluye código generado por el plugin de Compose Resources (Res.kt, ActualResourceCollectors.kt…):
    // no es código nuestro para formatear. NOTA: el plugin ktlint-gradle + KMP puede necesitar
    // `./gradlew ktlintCheck --rerun` tras un `clean` para que este filtro se aplique de forma
    // consistente a todos los source sets (comportamiento observado, no del todo determinista).
    filter {
        exclude { element ->
            val path = element.file.path.replace('\\', '/')
            path.contains("/generated/")
        }
    }
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
            implementation(compose.materialIconsExtended)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.androidx.navigation.compose)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.webview.multiplatform)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
            implementation(libs.androidx.webkit)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }

    jvmToolchain(17)
}

// FASE 8: genera un objeto Kotlin con la API key leída de local.properties (nunca committeado,
// ver CLAUDE.md §6) para que composeApp la use sin hardcodearla ni exponerla en el repo.
val generateApiKeys =
    tasks.register("generateApiKeys") {
        val localProps = Properties()
        val localPropsFile = rootProject.file("local.properties")
        if (localPropsFile.exists()) {
            localProps.load(localPropsFile.inputStream())
        }
        val apiKey = localProps.getProperty("FOOTBALL_DATA_API_KEY", "")
        // Raíz del source set generado; el paquete real es una subcarpeta de este directorio.
        val outputDir = layout.buildDirectory.dir("generated/apiKeys")

        inputs.property("footballDataApiKey", apiKey)
        outputs.dir(outputDir)

        doLast {
            val packageDir = outputDir.get().asFile.resolve("es/elchecf/app/core/network")
            packageDir.mkdirs()
            File(packageDir, "ApiKeys.kt").writeText(
                """
                package es.elchecf.app.core.network

                // Generado por :composeApp:generateApiKeys — no editar a mano, no se versiona.
                internal object ApiKeys {
                    const val FOOTBALL_DATA_API_KEY: String = "$apiKey"
                }
                """.trimIndent(),
            )
        }
    }

kotlin.sourceSets.commonMain {
    kotlin.srcDir(generateApiKeys.map { it.outputs.files.singleFile })
}

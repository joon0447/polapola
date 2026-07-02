import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.sqldelight)
}

val generateKakaoLocalApiSecrets by tasks.registering {
    val localPropertiesFile = rootProject.layout.projectDirectory.file("local.properties")
    val outputDirectory = layout.buildDirectory.dir("generated/kakaoLocalApiSecrets/commonMain/kotlin")

    inputs.file(localPropertiesFile).optional()
    outputs.dir(outputDirectory)

    doLast {
        val properties = Properties()
        val propertiesFile = localPropertiesFile.asFile
        if (propertiesFile.exists()) {
            propertiesFile.inputStream().use { inputStream ->
                properties.load(inputStream)
            }
        }

        val restApiKey =
            properties.getProperty("kakao.local.restApiKey")
                ?: properties.getProperty("KAKAO_LOCAL_REST_API_KEY")
                ?: ""
        val escapedRestApiKey =
            restApiKey
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
        val outputFile =
            outputDirectory
                .get()
                .asFile
                .resolve("com/joon/polapola/data/place/KakaoLocalApiSecrets.kt")

        outputFile.parentFile.mkdirs()
        outputFile.writeText(
            """
            package com.joon.polapola.data.place

            internal object KakaoLocalApiSecrets {
                const val REST_API_KEY: String = "$escapedRestApiKey"
            }
            """.trimIndent(),
        )
    }
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    androidLibrary {
        namespace = "com.joon.polapola.shared"
        compileSdk =
            libs
                .versions
                .android
                .compileSdk
                .get()
                .toInt()
        minSdk =
            libs
                .versions
                .android
                .minSdk
                .get()
                .toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    sourceSets {
        commonMain {
            kotlin.srcDir(layout.buildDirectory.dir("generated/kakaoLocalApiSecrets/commonMain/kotlin"))
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.compose.uiToolingPreview)
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.auth)
            implementation(libs.firebase.firestore)
            implementation(libs.sqldelight.android.driver)
        }
        iosMain.dependencies {
            implementation(libs.sqldelight.native.driver)
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
            implementation(libs.androidx.navigation.compose)
            implementation(libs.gitlive.firebase.auth)
            implementation(libs.gitlive.firebase.firestore)
            implementation(libs.gitlive.firebase.storage)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.datetime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

sqldelight {
    databases {
        create("PolapolaDatabase") {
            packageName.set("com.joon.polapola.data.local")
        }
    }
}

tasks
    .matching { task ->
        task.name in
            setOf(
                "compileAndroidMain",
                "compileKotlinIosArm64",
                "compileKotlinIosSimulatorArm64",
            )
    }.configureEach {
        dependsOn(generateKakaoLocalApiSecrets)
    }

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

ktlint {
    filter {
        exclude("**/resourceGenerator/**")
        exclude("**/build/**")
        exclude("**/generated/**")
    }
}

import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.aswinkumar.scrollstop"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.aswinkumar.scrollstop"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val releaseStoreFilePath = System.getenv("SCROLLSTOP_RELEASE_STORE_FILE")
                ?: project.findProperty("scrollstop.release.storeFile")?.toString()
            val releaseStorePassword = System.getenv("SCROLLSTOP_RELEASE_STORE_PASSWORD")
                ?: project.findProperty("scrollstop.release.storePassword")?.toString()
            val releaseKeyAlias = System.getenv("SCROLLSTOP_RELEASE_KEY_ALIAS")
                ?: project.findProperty("scrollstop.release.keyAlias")?.toString()
            val releaseKeyPassword = System.getenv("SCROLLSTOP_RELEASE_KEY_PASSWORD")
                ?: project.findProperty("scrollstop.release.keyPassword")?.toString()

            if (!releaseStoreFilePath.isNullOrBlank() && !releaseStorePassword.isNullOrBlank() && !releaseKeyAlias.isNullOrBlank() && !releaseKeyPassword.isNullOrBlank()) {
                storeFile = file(releaseStoreFilePath)
                storePassword = releaseStorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = if (
                !System.getenv("SCROLLSTOP_RELEASE_STORE_FILE").isNullOrBlank() &&
                !System.getenv("SCROLLSTOP_RELEASE_STORE_PASSWORD").isNullOrBlank() &&
                !System.getenv("SCROLLSTOP_RELEASE_KEY_ALIAS").isNullOrBlank() &&
                !System.getenv("SCROLLSTOP_RELEASE_KEY_PASSWORD").isNullOrBlank()
            ) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "17"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    add("ksp", libs.androidx.room.compiler)

    testImplementation(libs.junit)
    testImplementation("androidx.test:core:1.6.1")
    testImplementation("androidx.room:room-testing:2.6.1")
    testImplementation("org.robolectric:robolectric:4.14.1")
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}

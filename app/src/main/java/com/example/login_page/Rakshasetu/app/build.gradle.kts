plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)  // Add compose compiler plugin
}

android {
    namespace = "com.example.rakshasetu"
    compileSdk = 35  // Updated to 35

    defaultConfig {
        applicationId = "com.example.rakshasetu"
        minSdk = 24
        targetSdk = 35  // Updated to 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17  // Updated to 17
        targetCompatibility = JavaVersion.VERSION_17  // Updated to 17
    }

    kotlinOptions {
        jvmTarget = "17"  // Updated to 17
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)  // Explicit Kotlin stdlib
    implementation(libs.kotlin.stdlib.jdk7)
    implementation(libs.kotlin.stdlib.jdk8)

    // Rest of your dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)




    // Keep these compose dependencies
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.security.crypto.ktx)
    implementation(libs.firebase.firestore.ktx)


    // Consider removing these if not needed
    // implementation(libs.androidx.benchmark.macro)
    // implementation(libs.androidx.benchmark.traceprocessor.android)
    // implementation(libs.androidx.leanback)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

// Add this to enforce Kotlin version consistency
configurations.all {
    resolutionStrategy {
        eachDependency {
            if (requested.group == "org.jetbrains.kotlin") {
                useVersion(libs.versions.kotlin.get())
            }
        }
    }
}
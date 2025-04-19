// Top-level build file
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // Add the Compose plugin like this:
    id("org.jetbrains.compose") version "1.5.10" apply false
}
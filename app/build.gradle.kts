import com.android.build.api.dsl.BuildType
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    //alias(libs.plugins.kotlin.parcelize)
}


android {
    namespace = "eu.anifantakis.project.library.masterdetailmodern"
    compileSdk = 35

    defaultConfig {
        applicationId = "eu.anifantakis.project.library.masterdetailmodern"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    // add your API_KEY (if your app needs one) inside the "local.properties" file
    // example:
    // API_KEY=abcdefg12345

    val apiKey = gradleLocalProperties(project.rootDir, providers).getProperty("API_KEY")
    buildTypes {
        debug {
            isMinifyEnabled = false
            configureDebugBuildType(apiKey)
        }
        release {
            configureDebugBuildType(apiKey)
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Additional Icons
    implementation(libs.androidx.material.icons.extended)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // Koin
    implementation(libs.bundles.koin)
    implementation(libs.bundles.koin.compose)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    // Room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Ktor
    implementation(libs.bundles.ktor)

    // Timber for logging
    implementation(libs.timber)

    // Google Fonts
    // https://developer.android.com/develop/ui/compose/text/fonts#use-downloadable-fonts
    implementation(libs.androidx.ui.text.google.fonts)

    // Secured Android Persistence
    implementation(libs.secure.persist)
    implementation(libs.secure.persist.compose)

    // SplashScreen
    implementation(libs.androidx.core.splashscreen)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

private fun BuildType.configureDebugBuildType(apiKey: String) {
    buildConfigField("String", "API_KEY_MOVIES", "\"$apiKey\"")
    buildConfigField("String", "BASE_URL_MOVIES", "\"https://api.themoviedb.org\"")
    buildConfigField("String", "BASE_URL_AUTH", "\"https://dummyjson.com\"")
}

private fun BuildType.configureReleaseBuildType(apiKey: String) {
    buildConfigField("String", "API_KEY_MOVIES", "\"$apiKey\"")
    buildConfigField("String", "BASE_URL_MOVIES", "\"https://api.themoviedb.org\"")
    buildConfigField("String", "BASE_URL_AUTH", "\"https://dummyjson.com\"")
}
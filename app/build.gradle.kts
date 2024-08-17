plugins {
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.android.application")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
}
android {
    namespace = "com.example.hopedonationapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.hopedonationapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation("com.google.android.play:integrity:1.4.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation(libs.sdp.android)
    implementation(libs.ssp.android)

    implementation(libs.androidx.navigation.fragment.ktx.v276)
    implementation(libs.androidx.navigation.ui.ktx.v276)

    implementation(libs.androidx.lifecycle.extensions)

    implementation(libs.androidx.lifecycle.common.java8.v262)
    implementation(libs.androidx.lifecycle.viewmodel.ktx.v262)

    //fire base shit
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    //authentication phone
    implementation (libs.google.firebase.auth.ktx)
    implementation(libs.play.services.auth.api.phone.v1801)
    implementation (libs.play.services.auth.v2070)

    implementation(libs.google.firebase.storage.ktx)
    implementation(libs.google.firebase.database.ktx)
    implementation(libs.firebase.messaging.ktx.v2340)

    //image slider
//    implementation(libs.imageslideshow)


    //shimmer effect
    implementation(libs.shimmer)

    //room database
    val room_version = "2.6.1"
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
    implementation(libs.androidx.room.ktx)
    //glide
    implementation(libs.glide)
    annotationProcessor (libs.compiler)

    // phone pay


    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

}
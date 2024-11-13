plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.santeconnect"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.santeconnect"
        minSdk = 24
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
        packaging {
            resources {
                exclude("META-INF/NOTICE.md")
                exclude("META-INF/LICENSE.md")
                exclude("META-INF/NOTICE")
                exclude("META-INF/LICENSE")
                exclude("META-INF/DEPENDENCIES")

            }

        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }

    dependencies {

        implementation(libs.appcompat)
        implementation(libs.material)
        implementation(libs.activity)
        implementation(libs.constraintlayout)
        testImplementation(libs.junit)
        androidTestImplementation(libs.ext.junit)
        androidTestImplementation(libs.espresso.core)
        implementation("com.google.android.material:material:1.5.0")
        implementation("com.sun.mail:android-mail:1.6.7")
        implementation("com.sun.mail:android-activation:1.6.7")
        implementation("com.github.bumptech.glide:glide:4.12.0")
        annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
        implementation ("de.hdodenhof:circleimageview:3.1.0")
        implementation ("androidx.room:room-runtime:2.5.0")
        annotationProcessor ("androidx.room:room-compiler:2.5.0")


    }
}
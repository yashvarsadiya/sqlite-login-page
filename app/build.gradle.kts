plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.sqlitedatabasedemo1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sqlitedatabasedemo1"
        minSdk = 21
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.inappmessaging)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)



    // images cropper
    //noinspection GradleDynamicVersion
    implementation("com.theartofdev.edmodo:android-image-cropper:2.8.+")

    //circuler image view
    //noinspection UseTomlInstead
    implementation("com.github.mohammadatif:CircularImageView:1.0.0")

    //register page line dependency
    //noinspection UseTomlInstead
    implementation("com.android.support:design:29.0.0")

    //circular image dependency
    //noinspection UseTomlInstead
    implementation ("de.hdodenhof:circleimageview:3.1.0")

}
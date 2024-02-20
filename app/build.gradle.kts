plugins {
	id("com.android.application")
	kotlin("android")
	id("com.google.devtools.ksp")
	id("com.google.dagger.hilt.android")
}

android {
	namespace = "com.eva.scannerapp"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.eva.scannerapp"
		minSdk = 26
		targetSdk = 34
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
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
	buildFeatures {
		compose = true
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

	// core | compose |lifecycle
	implementation("androidx.core:core-ktx:1.12.0")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
	implementation("androidx.activity:activity-compose:1.8.2")
	//compose
	implementation(platform("androidx.compose:compose-bom:2024.02.00"))
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3")
	//dagger hilt
	implementation("com.google.dagger:hilt-android:2.50")
	ksp("com.google.dagger:hilt-android-compiler:2.50")
	//on device ml
	implementation("com.google.android.gms:play-services-mlkit-barcode-scanning:18.3.0")
	implementation("com.google.android.gms:play-services-mlkit-image-labeling:16.0.8")
	//camerax
	implementation("androidx.camera:camera-camera2:1.3.1")
	implementation("androidx.camera:camera-lifecycle:1.3.1")
	implementation("androidx.camera:camera-mlkit-vision:1.4.0-alpha04")
	//test
	testImplementation("junit:junit:4.13.2")
	// test-android
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
	androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
	androidTestImplementation("androidx.compose.ui:ui-test-junit4")
	//debug
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")
}
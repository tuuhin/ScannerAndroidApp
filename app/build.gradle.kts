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
	implementation(platform("androidx.compose:compose-bom:2024.02.02"))
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3")
	//dagger hilt
	implementation("com.google.dagger:hilt-android:2.50")
	ksp("com.google.dagger:hilt-android-compiler:2.50")
	//lifecycle-runtime-compose
	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
	implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
	//navigation
	implementation("io.github.raamcosta.compose-destinations:core:1.9.63")
	implementation("io.github.raamcosta.compose-destinations:animations-core:1.9.50")
	ksp("io.github.raamcosta.compose-destinations:ksp:1.9.63")
	implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
	//on device ml
	implementation("com.google.android.gms:play-services-mlkit-barcode-scanning:18.3.0")
	implementation("com.google.android.gms:play-services-mlkit-image-labeling:16.0.8")
	//camerax
	implementation("androidx.camera:camera-camera2:1.3.2")
	implementation("androidx.camera:camera-lifecycle:1.3.2")
	implementation("androidx.camera:camera-mlkit-vision:1.4.0-alpha04")
	//paging
	implementation("androidx.paging:paging-runtime-ktx:3.2.1")
	implementation("androidx.paging:paging-compose:3.2.1")
	//datastore
	implementation("androidx.datastore:datastore-preferences:1.0.0")
	//coil
	implementation("io.coil-kt:coil-compose:2.5.0")
	//splash
	implementation("androidx.core:core-splashscreen:1.0.1")
	//icons
	implementation("androidx.compose.material:material-icons-extended:1.6.3")
	//test
	testImplementation("junit:junit:4.13.2")
	// test-android
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
	androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.02"))
	androidTestImplementation("androidx.compose.ui:ui-test-junit4")
	//debug
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")
}
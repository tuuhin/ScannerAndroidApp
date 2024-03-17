package com.eva.scannerapp.presentation.feature_gallery.state

import android.os.Build

enum class GalleryPermissionState {
	UNKNOWN,
	NOT_GRANTED,
	PARTIALLY_GRANTED,
	GRANTED;


	val isPartiallyGranted: Boolean
		get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE && this == PARTIALLY_GRANTED

}
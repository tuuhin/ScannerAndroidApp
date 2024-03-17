package com.eva.scannerapp.presentation.feature_gallery.util

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.eva.scannerapp.presentation.feature_gallery.state.GalleryPermissionState

object GalleryPermissionChecker {

	fun checkPermission(context: Context): GalleryPermissionState {
		val fullReadAllowed =
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
				ContextCompat.checkSelfPermission(
					context,
					Manifest.permission.READ_MEDIA_IMAGES
				) == PermissionChecker.PERMISSION_GRANTED
			else ContextCompat.checkSelfPermission(
				context,
				Manifest.permission.READ_EXTERNAL_STORAGE
			) == PermissionChecker.PERMISSION_GRANTED
		// check on start if there are any changes
		if (fullReadAllowed) return GalleryPermissionState.GRANTED
		// on android 14 need to check if we have visual media selected read access
		val partialRead = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
			ContextCompat.checkSelfPermission(
				context,
				Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
			) == PermissionChecker.PERMISSION_GRANTED
		else false
		if (partialRead) return GalleryPermissionState.PARTIALLY_GRANTED
		return GalleryPermissionState.NOT_GRANTED
	}
}
package com.eva.scannerapp.presentation.feature_capture.composables

import android.app.ActionBar.LayoutParams
import android.util.Log
import android.view.ViewGroup
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

private const val CAMERA_PREVIEW_VIEW = "CAMERA_PREVIEW_VIEW"

@Composable
fun AndroidPreviewView(
	cameraController: LifecycleCameraController,
	modifier: Modifier = Modifier,
	onUpdate: (CameraController) -> Unit = {},
	viewConstraints: PreviewView.() -> Unit = {},
	backGround: Color = Color.Black,
) {
	val lifecycleOwner = LocalLifecycleOwner.current

	DisposableEffect(lifecycleOwner, cameraController) {
		val observer = LifecycleEventObserver { _, event ->
			if (event == Lifecycle.Event.ON_RESUME) {
				Log.d(CAMERA_PREVIEW_VIEW, "BOUNDED TO LIFECYCLE")
				cameraController.bindToLifecycle(lifecycleOwner)
			}
			if (event == Lifecycle.Event.ON_PAUSE) {
				cameraController.unbind()
				Log.d(CAMERA_PREVIEW_VIEW, "UN_BOUNDED")
			}
		}
		lifecycleOwner.lifecycle.addObserver(observer)
		onDispose {
			lifecycleOwner.lifecycle.removeObserver(observer)
		}
	}

	AndroidView(
		factory = { ctx ->
			PreviewView(ctx).apply {
				layoutParams = ViewGroup.LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT
				)
				controller = cameraController
				implementationMode = PreviewView.ImplementationMode.COMPATIBLE
				setBackgroundColor(backGround.toArgb())
				viewConstraints()
			}
		},
		update = { view -> view.controller?.let(onUpdate) },
		modifier = modifier,
	)
}
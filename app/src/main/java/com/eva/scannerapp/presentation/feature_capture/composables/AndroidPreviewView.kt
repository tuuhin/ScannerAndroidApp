package com.eva.scannerapp.presentation.feature_capture.composables

import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner

@Composable
fun AndroidPreviewView(
	cameraController: LifecycleCameraController,
	modifier: Modifier = Modifier,
	onUpdate: (CameraController) -> Unit = {},
	lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
	viewConstraints: PreviewView.() -> Unit = {},
) {
	DisposableEffect(lifecycleOwner, cameraController) {
		// bind the controller to lifecycle
		cameraController.bindToLifecycle(lifecycleOwner)
		onDispose {
			// unbind the controller
			cameraController.unbind()
		}
	}

	AndroidView(
		factory = { ctx ->
			PreviewView(ctx).apply {
				controller = cameraController
				viewConstraints()
			}
		},
		modifier = modifier,
		update = { it.controller?.let(onUpdate) }
	)
}
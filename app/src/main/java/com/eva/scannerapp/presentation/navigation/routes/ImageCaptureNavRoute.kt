package com.eva.scannerapp.presentation.navigation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eva.scannerapp.presentation.composables.UiEventsCollector
import com.eva.scannerapp.presentation.feature_capture.ImageCaptureScreen
import com.eva.scannerapp.presentation.feature_capture.ImageCaptureViewModel
import com.eva.scannerapp.presentation.navigation.screen.RouteAnimation
import com.eva.scannerapp.presentation.navigation.screen.Routes
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination(
	route = Routes.CAPTURE_ROUTE,
	style = RouteAnimation::class
)
@Composable
fun ImageCaptureNavRoute(
	navigator: DestinationsNavigator
) {
	val viewModel = hiltViewModel<ImageCaptureViewModel>()
	val previewState by viewModel.previewImage.collectAsStateWithLifecycle()
	val screenState by viewModel.screenState.collectAsStateWithLifecycle()

	UiEventsCollector(viewModel = viewModel)

	ImageCaptureScreen(
		previewState = previewState,
		screenState = screenState,
		onCapture = { bitmap ->

		},
		onNavigateGallery = {
			navigator.navigate(route = Routes.GALLERY_ROUTE, onlyIfResumed = true) {
				launchSingleTop = true
			}
		},
		onEvent = viewModel::onEvent,
	)

}
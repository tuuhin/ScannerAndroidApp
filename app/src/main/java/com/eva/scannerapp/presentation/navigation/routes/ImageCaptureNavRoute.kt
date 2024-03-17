package com.eva.scannerapp.presentation.navigation.routes

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eva.scannerapp.presentation.feature_capture.ImageCaptureScreen
import com.eva.scannerapp.presentation.feature_capture.ImageCaptureViewModel
import com.eva.scannerapp.presentation.feature_capture.composables.CapturingImageDialog
import com.eva.scannerapp.presentation.feature_capture.util.addImageAnalyzer
import com.eva.scannerapp.presentation.navigation.navArgs.ResultsScreenArgs
import com.eva.scannerapp.presentation.navigation.routes.destinations.ResultsNavRouteDestination
import com.eva.scannerapp.presentation.navigation.screen.RouteAnimation
import com.eva.scannerapp.presentation.navigation.screen.Routes
import com.eva.scannerapp.presentation.util.LocalSnackBarStateProvider
import com.eva.scannerapp.presentation.util.viewmodel.SideEffects
import com.eva.scannerapp.presentation.util.viewmodel.UiEvents
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@RootNavGraph(start = true)
@Destination(
	route = Routes.CAPTURE_ROUTE,
	style = RouteAnimation::class
)
@Composable
fun ImageCaptureNavRoute(
	navigator: DestinationsNavigator
) {
	val context = LocalContext.current
	val snackBarProvider = LocalSnackBarStateProvider.current

	val viewModel = hiltViewModel<ImageCaptureViewModel>()

	val previewState by viewModel.previewImageState.collectAsStateWithLifecycle()
	val screenState by viewModel.cameraScreenState.collectAsStateWithLifecycle()
	val isCapturing by viewModel.isCapturing.collectAsStateWithLifecycle()
	val recognizedItem by viewModel.recognizedItemState.collectAsStateWithLifecycle()

	val scope = rememberCoroutineScope()

	viewModel.SideEffects { events ->
		when (events) {
			is UiEvents.ShowSnackBar -> snackBarProvider.showSnackbar(events.message)
			is UiEvents.ShowToast -> Toast.makeText(context, events.message, Toast.LENGTH_SHORT)
				.show()

			is UiEvents.NavigateBack -> navigator.popBackStack()
			is UiEvents.NavigateToResults -> {
				val navArgs = ResultsScreenArgs(fileUri = events.uri, fromCamera = true)
				navigator.navigate(ResultsNavRouteDestination(navArgs))
			}
		}
	}

	CapturingImageDialog(isCapturing = isCapturing)

	ImageCaptureScreen(
		previewImageState = previewState,
		cameraScreenState = screenState,
		recognizedState = recognizedItem,
		navigation = {
			navigator.navigate(route = Routes.GALLERY_ROUTE, onlyIfResumed = true) {
				launchSingleTop = true
			}
		},
		onSettingsNavigation = {
			navigator.navigate(route = Routes.SETTINGS_ROUTE, onlyIfResumed = true) {
				launchSingleTop = true
			}
		},
		onImageEvents = viewModel::onImageEvents,
		onSheetEvents = viewModel::onSheetEvents,
		controllerConstraints = { controller ->
			viewModel.imageAnalyzer.onEach { analyzer ->
				// clear the analyzer
				controller.clearImageAnalysisAnalyzer()
				//set the new analyzer
				controller.addImageAnalyzer(context = context, analyzer)
			}.launchIn(scope)
		}
	)
}
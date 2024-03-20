package com.eva.scannerapp.presentation.feature_capture.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.R
import com.eva.scannerapp.presentation.feature_capture.states.ImagePreviewState
import com.eva.scannerapp.presentation.util.preview.PreviewFakes
import com.eva.scannerapp.ui.theme.ScannerAppTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun CameraControls(
	isEnabled: Boolean,
	imageState: ImagePreviewState,
	onPreviewClick: () -> Unit,
	onCapture: () -> Unit,
	modifier: Modifier = Modifier,
) {

	var ranCaptureAnimation by remember { mutableStateOf(false) }

	LaunchedEffect(ranCaptureAnimation, isEnabled) {
		// if enabled is false, then no need to run the animation
		if (isEnabled) {
			// change the value after 5 seconds
			delay(5.seconds)
			ranCaptureAnimation = !ranCaptureAnimation
		}
	}

	Column(
		modifier = modifier
			.padding(horizontal = dimensionResource(id = R.dimen.camera_controls_padding_horizontal)),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(4.dp)
	) {
		AnimatedVisibility(
			visible = isEnabled,
			enter = slideInVertically(),
			exit = slideOutVertically()
		) {
			Box(
				modifier = Modifier
					.wrapContentHeight()
					.background(
						color = Color.DarkGray.copy(alpha = .75f),
						shape = MaterialTheme.shapes.large
					)
			) {
				Text(
					text = stringResource(id = R.string.shutter_button_helper_text),
					color = Color.White,
					style = MaterialTheme.typography.bodyLarge,
					modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
				)
			}
		}
		Box(
			modifier = modifier
				.defaultMinSize(minHeight = dimensionResource(id = R.dimen.camera_controls_height)),
		) {
			PreviewPreviousImage(
				imageModel = imageState.image,
				onClick = onPreviewClick,
				modifier = Modifier
					.align(Alignment.CenterStart)
					.wrapContentSize()
			)

			AnimatedCaptureButton(
				isAnimationRunning = isEnabled && ranCaptureAnimation,
				onClick = onCapture,
				modifier = Modifier.align(Alignment.Center)
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_capture),
					contentDescription = stringResource(id = R.string.camera_capture_icon_desc),
					tint = Color.Black
				)
			}
		}
	}
}

@Preview
@Composable
private fun CameraControlsWithLoadedPreview() = ScannerAppTheme {
	CameraControls(
		onCapture = {},
		onPreviewClick = {},
		imageState = ImagePreviewState(
			isLoading = false,
			image = PreviewFakes.FAKE_IMAGE_DATA_MODEL
		),
		isEnabled = true,
		modifier = Modifier.fillMaxWidth()
	)
}

@Preview
@Composable
private fun CameraControlsWithLoadingLoadedPreview() = ScannerAppTheme {
	CameraControls(
		onCapture = {},
		onPreviewClick = {},
		imageState = ImagePreviewState(
			isLoading = true,
			image = PreviewFakes.FAKE_IMAGE_DATA_MODEL
		),
		isEnabled = true,
		modifier = Modifier.fillMaxWidth()
	)
}
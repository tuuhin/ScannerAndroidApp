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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.R
import com.eva.scannerapp.presentation.feature_capture.states.ImagePreviewState
import com.eva.scannerapp.presentation.util.preview.PreviewApi33
import com.eva.scannerapp.presentation.util.preview.PreviewFakes
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun CameraControls(
	isEnabled: Boolean,
	onImageCapture: () -> Unit,
	modifier: Modifier = Modifier,
	imageState: ImagePreviewState,
) {
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
					text = stringResource(id = R.string.shutter_button_text),
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
			this@Column.AnimatedVisibility(
				visible = imageState.isAvailable,
				modifier = Modifier.align(Alignment.CenterStart)
			) {

				PreviewPreviousImage(
					imageUri = imageState.image?.imageUri,
				)
			}
			AnimatedCaptureButton(
				isEnabled = isEnabled,
				onClick = onImageCapture,
				modifier = Modifier.align(Alignment.Center)
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_capture),
					contentDescription = stringResource(id = R.string.capture_icon__des),
					tint = Color.Black
				)
			}
		}
	}
}

@PreviewApi33
@Composable
fun CameraControlsPreview() = ScannerAppTheme {
	CameraControls(
		onImageCapture = {},
		imageState = ImagePreviewState(
			isLoading = false,
			image = PreviewFakes.FAKE_IMAGE_DATA_MODEL
		),
		isEnabled = true,
		modifier = Modifier.fillMaxWidth()
	)
}
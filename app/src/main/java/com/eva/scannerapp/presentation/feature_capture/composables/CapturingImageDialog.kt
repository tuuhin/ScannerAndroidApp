package com.eva.scannerapp.presentation.feature_capture.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.eva.scannerapp.R
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CapturingImageDialog(
	isCapturing: Boolean,
	modifier: Modifier = Modifier,
) {
	if (!isCapturing) return

	val properties = remember {
		DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
	}

	BasicAlertDialog(
		onDismissRequest = {},
		modifier = modifier.sizeIn(minWidth = 80.dp, minHeight = 80.dp),
		properties = properties
	) {
		Surface(
			shape = MaterialTheme.shapes.large,
			color = MaterialTheme.colorScheme.surfaceVariant,
			modifier = Modifier.wrapContentSize(),
		) {
			Column(
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier.padding(all = dimensionResource(id = R.dimen.scaffold_padding))
			) {
				CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
				Spacer(modifier = Modifier.height(8.dp))
				Text(
					text = stringResource(id = R.string.capturing_image_dialog_text),
					style = MaterialTheme.typography.titleMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		}
	}
}

@PreviewLightDark
@Composable
private fun CapturingImageDialogPreview() = ScannerAppTheme {
	CapturingImageDialog(isCapturing = true)
}
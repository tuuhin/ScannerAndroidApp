package com.eva.scannerapp.presentation.feature_capture.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.R
import com.eva.scannerapp.presentation.util.preview.PreviewLightDarkApi33
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun CameraPermissionPlaceHolder(
	onAllowAccess: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_8)),
		modifier = modifier.padding(horizontal = 20.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = stringResource(id = R.string.request_read_camera_perms_text),
			style = MaterialTheme.typography.titleLarge,
			textAlign = TextAlign.Center,
		)
		CheckCameraPermissionsButton(
			onPermissionChanged = onAllowAccess,
			shape = MaterialTheme.shapes.extraLarge,
			modifier = Modifier.fillMaxWidth(),
			colors = ButtonDefaults.buttonColors(
				containerColor = MaterialTheme.colorScheme.secondary,
				contentColor = MaterialTheme.colorScheme.onSecondary
			),
			contentPadding = PaddingValues(
				horizontal = ButtonDefaults.ContentPadding
					.calculateRightPadding(LayoutDirection.Ltr),
				vertical = 12.dp
			)
		) {
			Icon(imageVector = Icons.Outlined.CameraAlt, contentDescription = null)
			Text(
				text = stringResource(id = R.string.allow_access_text),
				style = MaterialTheme.typography.titleMedium,
				textAlign = TextAlign.Center,
				modifier = Modifier.weight(1f)
			)
		}
	}
}


@PreviewLightDarkApi33
@Composable
fun CameraPermissionPlaceHolderPreview() = ScannerAppTheme {
	Surface {
		CameraPermissionPlaceHolder(
			onAllowAccess = {},
			modifier = Modifier
				.padding(vertical = 12.dp)
				.wrapContentHeight(),
		)
	}
}
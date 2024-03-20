package com.eva.scannerapp.presentation.feature_capture.composables

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.R
import com.eva.scannerapp.presentation.composables.CheckSinglePermissionButton
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun CameraPermissionPlaceHolder(
	onAllowAccess: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
) {
	val permission = Manifest.permission.CAMERA

	Column(
		verticalArrangement = Arrangement
			.spacedBy(space = dimensionResource(id = R.dimen.space_8)),
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier.padding(horizontal = 20.dp),
	) {
		Text(
			text = stringResource(id = R.string.camera_permission_text),
			style = MaterialTheme.typography.titleLarge,
			textAlign = TextAlign.Center,
		)
		CheckSinglePermissionButton(
			permission = permission,
			isPermissionAllowed = onAllowAccess,
			modifier = Modifier
				.defaultMinSize(minWidth = dimensionResource(id = R.dimen.perms_button_min_width)),
		) {
			Icon(
				painter = painterResource(id = R.drawable.ic_camera),
				contentDescription = null
			)
			Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8)))
			Text(
				text = stringResource(id = R.string.allow_permissions_text),
				style = MaterialTheme.typography.titleMedium,
				textAlign = TextAlign.Center,
			)
		}
	}
}


@PreviewLightDark
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
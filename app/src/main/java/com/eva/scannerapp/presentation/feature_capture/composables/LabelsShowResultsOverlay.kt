package com.eva.scannerapp.presentation.feature_capture.composables

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.R
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun LabelsShowResultOverlay(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	color: Color = Color.White,
	interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
	Column(
		modifier = modifier
			.defaultMinSize(minHeight = 64.dp)
			.wrapContentHeight()
			.padding(all = 4.dp)
			.clickable(
				role = Role.Button,
				onClick = onClick,
				interactionSource = interactionSource,
				indication = null
			),
		verticalArrangement = Arrangement.spacedBy(8.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Image(
			painter = painterResource(id = R.drawable.ic_label_rope),
			contentDescription = "Label Results",
			colorFilter = ColorFilter.tint(color = color),
			modifier = Modifier.defaultMinSize(minWidth = 56.dp, minHeight = 56.dp)
		)
		Text(
			text = "Check Analysis",
			style = MaterialTheme.typography.titleLarge,
		)
	}
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun LabelsShowResultsOverlayPreview() = ScannerAppTheme {
	Surface {
		LabelsShowResultOverlay(onClick = {})
	}
}
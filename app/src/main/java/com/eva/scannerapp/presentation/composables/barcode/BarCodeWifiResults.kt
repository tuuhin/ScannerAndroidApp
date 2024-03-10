
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.domain.ml.util.BarCodeTypes
import com.eva.scannerapp.presentation.util.preview.PreviewFakes
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun BarCodeWifiResults(
	type: BarCodeTypes.WiFi,
	modifier: Modifier = Modifier,
) {
	Column(modifier = modifier) {
		SuggestionChip(
			onClick = {
// TODO: Configure it Latter

			},
			label = { Text(text = "Join Network") },
			icon = {
				Icon(
					imageVector = Icons.Default.Wifi,
					contentDescription = null,
				)
			},
			shape = MaterialTheme.shapes.large,
			colors = SuggestionChipDefaults
				.suggestionChipColors(
					iconContentColor = MaterialTheme.colorScheme.primary,
					containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
				)
		)
		Row(
			horizontalArrangement = Arrangement.spacedBy(32.dp),
		) {
			Column(
				verticalArrangement = Arrangement.SpaceBetween,
				modifier = Modifier.wrapContentHeight()
			) {
				Text(
					text = "Network Name",
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
				Text(
					text = "Password",
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
			Column(
				modifier = Modifier.align(Alignment.CenterVertically)
			) {
				Text(
					text = type.ssid ?: "",
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.onSurface
				)
				Text(
					text = type.password ?: "",
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.onSurface
				)
			}
		}
	}
}

@PreviewLightDark
@Composable
fun BarCodeWifiResultsPreview() = ScannerAppTheme {
	Surface {
		BarCodeWifiResults(
			type = PreviewFakes.FAKE_QR_CODE_WIFI.type as BarCodeTypes.WiFi,
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp, vertical = 8.dp)
		)
	}
}

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.domain.ml.util.BarCodeTypes
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun BarCodeUrlResults(
	type: BarCodeTypes.UrlBookMark,
	modifier: Modifier = Modifier
) {
	val context = LocalContext.current

	Column(modifier = modifier) {
		SuggestionChip(
			onClick = {
				type.url?.let { site ->
					val intent = Intent(Intent.ACTION_VIEW).apply {
						data = Uri.parse(site)
						putExtra(Intent.EXTRA_TITLE, type.title)
						putExtra(Intent.EXTRA_TEXT, site)
					}
					context.startActivity(intent)
				}
			},
			label = { Text(text = "Open Site") },
			icon = {
				Icon(
					imageVector = Icons.Default.Public,
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
					text = "Title",
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
				Text(
					text = "URL",
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
			Column(
				modifier = Modifier.align(Alignment.CenterVertically)
			) {
				Text(
					text = type.title ?: "",
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.onSurface
				)
				Text(
					text = type.url ?: "",
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.onSurface
				)
			}
		}
	}
}

@PreviewLightDark
@Composable
private fun BarCodeUrlResultsPreview() = ScannerAppTheme {
	Surface {
		BarCodeUrlResults(
			type = BarCodeTypes.UrlBookMark(
				title = "hey this is an awesome website",
				url = "https://google.com"
			),
			modifier = Modifier
				.padding(horizontal = 16.dp, vertical = 8.dp)
		)
	}
}
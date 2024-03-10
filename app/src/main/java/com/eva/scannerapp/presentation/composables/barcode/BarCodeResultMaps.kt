
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.domain.ml.util.BarCodeTypes
import java.text.DecimalFormat

@Composable
fun BarCodeResultMaps(
	type: BarCodeTypes.GeoPoint,
	modifier: Modifier = Modifier
) {
	val formatter = remember {
		DecimalFormat.getInstance()
	}
	val context = LocalContext.current

	Column(modifier = modifier) {
		SuggestionChip(
			onClick = {
				val uri = "geo:${type.lat},${type.long}"
				val intent = Intent(Intent.ACTION_VIEW).apply {
					data = Uri.parse(uri)
				}
				context.startActivity(intent)
			},
			label = { Text(text = "Open Map") },
			icon = {
				Icon(
					imageVector = Icons.Default.Map,
					contentDescription = null,
				)
			},
			shape = MaterialTheme.shapes.large,
			colors = SuggestionChipDefaults.suggestionChipColors(
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
				type.lat?.let {
					Text(
						text = "Latitude",
						style = MaterialTheme.typography.labelLarge,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)

				}
				type.long?.let {
					Text(
						text = "Longitude",
						style = MaterialTheme.typography.labelLarge,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
				}
			}
			Column(
				modifier = Modifier.align(Alignment.CenterVertically)
			) {
				type.lat?.let { lat ->
					Text(
						text = formatter.format(lat),
						style = MaterialTheme.typography.labelMedium,
						color = MaterialTheme.colorScheme.onSurface
					)

				}
				type.long?.let { long ->
					Text(
						text = formatter.format(long),
						style = MaterialTheme.typography.labelMedium,
						color = MaterialTheme.colorScheme.onSurface
					)
				}
			}
		}
	}
}
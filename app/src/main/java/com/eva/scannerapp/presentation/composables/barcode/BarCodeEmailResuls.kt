
import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.domain.ml.util.BarCodeTypes
import com.eva.scannerapp.presentation.util.preview.PreviewFakes
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun BarCodeEmailResults(
	type: BarCodeTypes.Email,
	modifier: Modifier = Modifier
) {
	val context = LocalContext.current

	Column(modifier = modifier) {
		SuggestionChip(
			onClick = {
				try {
					val intent = Intent(Intent.ACTION_SENDTO).apply {
						putExtra(Intent.EXTRA_TITLE, type.subject)
						putExtra(Intent.EXTRA_TEXT, type.body)
						putExtra(Intent.EXTRA_EMAIL, type.address)
					}
					context.startActivity(intent)

				} catch (e: ActivityNotFoundException) {
					Toast.makeText(context, "No activity found", Toast.LENGTH_SHORT).show()
					e.printStackTrace()
				}

			},
			label = { Text(text = "Send Mail") },
			icon = {
				Icon(
					imageVector = Icons.Outlined.Email,
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
				type.address?.let {
					Text(
						text = "Address",
						style = MaterialTheme.typography.labelLarge,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
				}
				type.subject?.let {
					Text(
						text = "Subject",
						style = MaterialTheme.typography.labelLarge,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
				}
				Text(
					text = "Body",
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
			Column(
				modifier = Modifier.align(Alignment.CenterVertically)
			) {
				type.address?.let {
					Text(
						text = type.address,
						style = MaterialTheme.typography.labelMedium,
						color = MaterialTheme.colorScheme.onSurface
					)
				}
				type.subject?.let {
					Text(
						text = type.subject,
						style = MaterialTheme.typography.labelMedium,
						color = MaterialTheme.colorScheme.onSurface
					)
				}
				type.body?.let {
					Text(
						text = type.body,
						style = MaterialTheme.typography.labelMedium,
						color = MaterialTheme.colorScheme.onSurface,
						maxLines = 2,
						overflow = TextOverflow.Ellipsis
					)
				}
			}
		}
	}
}

@PreviewLightDark
@Composable
private fun BarCodeUrlResultsPreview() = ScannerAppTheme {
	Surface {
		BarCodeEmailResults(
			type = PreviewFakes.FAKE_QR_CODE_EMAIL.type as BarCodeTypes.Email,
			modifier = Modifier
				.padding(horizontal = 16.dp, vertical = 8.dp)
		)
	}
}
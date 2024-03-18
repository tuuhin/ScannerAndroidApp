package com.eva.scannerapp.presentation.composables.barcode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
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
import com.eva.scannerapp.R
import com.eva.scannerapp.domain.ml.util.BarCodeTypes
import com.eva.scannerapp.presentation.util.preview.PreviewFakes
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun BarCodeResultsContacts(
	type: BarCodeTypes.ContactInfo,
	modifier: Modifier = Modifier
) {

	val emails = remember {
		type.emails.filterIsInstance<BarCodeTypes.Email>()
			.mapNotNull { it.address }
	}

	val phones = remember {
		type.phones.filterIsInstance<BarCodeTypes.Phone>()
			.mapNotNull { it.number }
	}

	val urls = remember {
		type.urls.filterIsInstance<BarCodeTypes.UrlBookMark>()
			.mapNotNull { it.url }
	}

	val addresses = remember { type.addresses.filterNotNull() }


	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(4.dp)
	) {
		Row(
			horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.barcode_resutls_spacing)),
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.wrapContentHeight()
		) {
			Column(modifier = Modifier.width(IntrinsicSize.Max)) {
				type.name?.let {
					Text(
						text = "Person Name",
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
				}
				type.title?.let {
					Text(
						text = "Title",
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
				}
				type.organization?.let {
					Text(
						text = "Organization",
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
				}
			}
			Column(modifier = Modifier.height(IntrinsicSize.Max)) {
				type.name?.let { _ ->
					Text(
						text = type.name.displayName,
						style = MaterialTheme.typography.labelLarge,
						color = MaterialTheme.colorScheme.onSurface
					)
				}
				type.title?.let { title ->
					Text(
						text = title,
						style = MaterialTheme.typography.labelMedium,
						color = MaterialTheme.colorScheme.onSurface
					)
				}
				type.organization?.let { organization ->


					Text(
						text = organization,
						style = MaterialTheme.typography.labelMedium,
						color = MaterialTheme.colorScheme.onSurface
					)

				}
			}
		}
		if (emails.isNotEmpty()) {
			HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
			Row(
				horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.barcode_resutls_spacing)),
				verticalAlignment = Alignment.Top
			) {
				Text(
					text = stringResource(id = R.string.barcode_results_title_contacts_emails),
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
				Column {
					emails.forEach { mail ->
						Text(
							text = mail,
							style = MaterialTheme.typography.labelMedium,
							color = MaterialTheme.colorScheme.onSurface
						)
					}
				}
			}
		}
		if (phones.isNotEmpty()) {
			HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
			Row(
				horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.barcode_resutls_spacing))
			) {
				Text(
					text = stringResource(id = R.string.barcode_results_title_contacts_contact_number),
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
				Column {
					phones.forEach { phone ->
						Text(
							text = phone,
							style = MaterialTheme.typography.labelMedium,
							color = MaterialTheme.colorScheme.onSurface
						)
					}
				}
			}
		}
		if (urls.isNotEmpty()) {
			HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
			Row(
				horizontalArrangement = Arrangement.spacedBy(32.dp)
			) {
				Text(
					text = stringResource(id = R.string.barcode_results_title_contacts_websites),
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
				Column {
					urls.forEach { url ->
						Text(
							text = url,
							style = MaterialTheme.typography.labelMedium,
							color = MaterialTheme.colorScheme.onSurface
						)
					}
				}
			}
		}
		if (addresses.isNotEmpty()) {
			HorizontalDivider(
				color = MaterialTheme.colorScheme.outlineVariant
			)
			Row(
				horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.barcode_resutls_spacing))
			) {
				Text(
					text = stringResource(id = R.string.barcode_results_title_contacts_address),
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
				Column {
					addresses.forEach { address ->
						Text(
							text = address,
							style = MaterialTheme.typography.labelMedium,
							color = MaterialTheme.colorScheme.onSurface
						)
					}
				}
			}
		}
	}

}

@PreviewLightDark
@Composable
private fun BarCodeResultsContactsPreview() = ScannerAppTheme {
	Surface {
		BarCodeResultsContacts(
			type = PreviewFakes.FAKE_QR_CODE_CONTACTS.type as BarCodeTypes.ContactInfo,
			modifier = Modifier
				.padding(horizontal = 16.dp, vertical = 8.dp)
		)
	}
}
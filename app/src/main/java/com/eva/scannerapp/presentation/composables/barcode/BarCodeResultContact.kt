package com.eva.scannerapp.presentation.composables.barcode

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.domain.ml.util.BarCodeTypes

@Composable
fun BarCodeResultsContacts(
	type: BarCodeTypes.ContactInfo,
	modifier: Modifier = Modifier
) {
	val context = LocalContext.current

	val emails = remember {
		type.emails.filterIsInstance<BarCodeTypes.Email>().filter { it.address != null }
	}

	val phones = remember {
		type.phones.filterIsInstance<BarCodeTypes.Phone>().filter { it.number != null }
	}

	val urls = remember {
		type.urls.filterIsInstance<BarCodeTypes.UrlBookMark>().filter { it.url != null }
	}

	val addresses = remember { type.addresses.filterNotNull() }


	Column(modifier = modifier) {
		SuggestionChip(
			onClick = {
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
		Column(
			verticalArrangement = Arrangement.SpaceBetween,
			modifier = Modifier.wrapContentHeight()
		) {
			type.name?.let { personName ->
				Row(
					horizontalArrangement = Arrangement.spacedBy(32.dp),
				) {
					Text(
						text = "Person Name",
						style = MaterialTheme.typography.labelLarge,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
					Text(
						text = buildString {
							if (personName.pronunciation != null)
								append(personName.pronunciation)
							append(" ")
							if (personName.prefix != null) {
								append(personName.prefix)
								append(" ")
							}
							if (personName.formattedName != null)
								append(personName.formattedName)
							else {
								if (personName.first != null) {
									append(personName.first)
									append(" ")
								}
								if (personName.middle != null) {
									append(personName.middle)
									append(" ")
								}
								if (personName.last != null) {
									append(personName.last)
								}
							}
							append(" ")
							if (personName.suffix != null) {
								append(personName.suffix)
								append(" ")
							}
						},
					)
				}
			}
			type.title?.let { title ->
				Row(
					horizontalArrangement = Arrangement.spacedBy(32.dp)
				) {
					Text(
						text = "Title",
						style = MaterialTheme.typography.labelLarge,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
					Text(
						text = title,
						style = MaterialTheme.typography.labelMedium,
						color = MaterialTheme.colorScheme.onSurface
					)
				}
			}
			type.organization?.let { organization ->
				Row(
					horizontalArrangement = Arrangement.spacedBy(32.dp)
				) {
					Text(
						text = "Organization",
						style = MaterialTheme.typography.labelLarge,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
					Text(
						text = organization,
						style = MaterialTheme.typography.labelMedium,
						color = MaterialTheme.colorScheme.onSurface
					)
				}
			}
			if (emails.isNotEmpty()) {
				HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
				Row(
					horizontalArrangement = Arrangement.spacedBy(32.dp)
				) {
					Text(
						text = "Emails", style = MaterialTheme.typography.labelLarge,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
					Column {
						emails.forEach { mail ->
							TextButton(
								onClick = {
									val intent = Intent(Intent.ACTION_SEND).apply {
										putExtra(Intent.EXTRA_TITLE, mail.subject)
										putExtra(Intent.EXTRA_TEXT, mail.body)
										putExtra(Intent.EXTRA_EMAIL, mail.address)
									}
									context.startActivity(intent)
								},
							) {
								Text(
									text = mail.address ?: "",
									style = MaterialTheme.typography.labelMedium,
									color = MaterialTheme.colorScheme.onSurface
								)
							}
						}
					}
				}
			}
			if (phones.isNotEmpty()) {
				HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
				Row(
					horizontalArrangement = Arrangement.spacedBy(32.dp)
				) {
					Text(
						text = "Phones",
						style = MaterialTheme.typography.labelLarge,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
					Column {
						phones.forEach { phone ->
							Text(
								text = phone.number ?: "",
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
						text = "Urls",
						style = MaterialTheme.typography.labelLarge,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
					Column {
						urls.forEach { url ->
							Text(
								text = url.url ?: "",
								style = MaterialTheme.typography.labelMedium,
								color = MaterialTheme.colorScheme.onSurface
							)
						}
					}
				}
			}
			if (addresses.isNotEmpty()) {
				HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
				Row(
					horizontalArrangement = Arrangement.spacedBy(32.dp)
				) {
					Text(
						text = "Addresses",
						style = MaterialTheme.typography.labelLarge,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
					Column {
						addresses.forEach { address ->
							Text(
								text = address ?: "",
								style = MaterialTheme.typography.labelMedium,
								color = MaterialTheme.colorScheme.onSurface
							)
						}
					}
				}
			}
		}
	}
}
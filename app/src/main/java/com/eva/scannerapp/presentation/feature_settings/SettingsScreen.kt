package com.eva.scannerapp.presentation.feature_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.R
import com.eva.scannerapp.presentation.composables.ManagePermissionMenu
import com.eva.scannerapp.presentation.feature_settings.composables.ClearCacheOption
import com.eva.scannerapp.presentation.feature_settings.composables.SaveToExternalStorageOption
import com.eva.scannerapp.presentation.feature_settings.composables.settingsAboutAvailableAnalyzer
import com.eva.scannerapp.presentation.feature_settings.state.SettingsScreenEvents
import com.eva.scannerapp.presentation.feature_settings.state.SettingsScreenState
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
	state: SettingsScreenState,
	onEvent: (SettingsScreenEvents) -> Unit,
	modifier: Modifier = Modifier,
	navigation: (@Composable () -> Unit)? = null,
) {
	val context = LocalContext.current
	var showMenu by remember { mutableStateOf(false) }

	val scrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

	Scaffold(
		topBar = {
			MediumTopAppBar(
				title = {
					Text(
						text = stringResource(id = R.string.settings_route_title),
						style = MaterialTheme.typography.headlineSmall
					)
				},
				navigationIcon = navigation ?: {},
				actions = {
					ManagePermissionMenu(
						isExpanded = showMenu,
						onClick = { showMenu = true },
						onDismissRequest = { showMenu = false },
					)
				},
				scrollBehavior = scrollBehaviour
			)
		},
		modifier = modifier,
	) { scPadding ->
		LazyColumn(
			modifier = Modifier
				.padding(horizontal = dimensionResource(id = R.dimen.scaffold_padding))
				.nestedScroll(scrollBehaviour.nestedScrollConnection),
			verticalArrangement = Arrangement.spacedBy(4.dp),
			contentPadding = scPadding
		) {
			//options
			item {
				SaveToExternalStorageOption(
					isExternalStorageAllowed = state.isExternalSaveAllowed,
					onToggleOption = { onEvent(SettingsScreenEvents.ToggleSaveToExternalStorage) },
				)
			}
			item {
				ClearCacheOption(
					onClearCaptureCache = { onEvent(SettingsScreenEvents.OnClearImageCache) },
				)
			}
			// analyzers
			settingsAboutAvailableAnalyzer(context = context)
		}
	}
}

@PreviewLightDark
@Composable
private fun SettingsScreenPreview() = ScannerAppTheme {
	SettingsScreen(
		state = SettingsScreenState(),
		onEvent = {},
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Filled.ArrowBack,
				contentDescription = "Back Arrow"
			)
		},
	)
}
package com.eva.scannerapp.presentation.navigation.routes

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eva.scannerapp.R
import com.eva.scannerapp.presentation.feature_settings.SettingsScreen
import com.eva.scannerapp.presentation.feature_settings.SettingsViewModel
import com.eva.scannerapp.presentation.navigation.screen.RouteAnimation
import com.eva.scannerapp.presentation.navigation.screen.Routes
import com.eva.scannerapp.presentation.util.LocalSnackBarStateProvider
import com.eva.scannerapp.presentation.util.viewmodel.SideEffects
import com.eva.scannerapp.presentation.util.viewmodel.UiEvents
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(
	route = Routes.SETTINGS_ROUTE,
	style = RouteAnimation::class,
)
@Composable
fun SettingsNavRoute(
	navigator: DestinationsNavigator,
) {

	val context = LocalContext.current
	val snackBarProvider = LocalSnackBarStateProvider.current

	val viewModel = hiltViewModel<SettingsViewModel>()

	val settingsState by viewModel.settingsState.collectAsStateWithLifecycle()

	viewModel.SideEffects { events ->
		when (events) {
			is UiEvents.ShowSnackBar -> snackBarProvider.showSnackbar(events.message)
			is UiEvents.ShowToast -> Toast.makeText(context, events.message, Toast.LENGTH_SHORT)
				.show()

			else -> {}
		}
	}

	SettingsScreen(
		state = settingsState,
		onEvent = viewModel::onEvents,
		navigation = {
			IconButton(onClick = navigator::popBackStack) {
				Icon(
					imageVector = Icons.AutoMirrored.Filled.ArrowBack,
					contentDescription = stringResource(id = R.string.navigation_back)
				)
			}
		},
	)
}
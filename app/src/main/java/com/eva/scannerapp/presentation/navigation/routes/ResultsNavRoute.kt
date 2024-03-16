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
import com.eva.scannerapp.presentation.feature_result.AnalysisResultsViewModel
import com.eva.scannerapp.presentation.feature_result.ResultsScreen
import com.eva.scannerapp.presentation.navigation.navArgs.ResultsScreenArgs
import com.eva.scannerapp.presentation.navigation.screen.RouteAnimation
import com.eva.scannerapp.presentation.navigation.screen.Routes
import com.eva.scannerapp.presentation.util.LocalSnackBarStateProvider
import com.eva.scannerapp.presentation.util.viewmodel.SideEffects
import com.eva.scannerapp.presentation.util.viewmodel.UiEvents
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(
	route = Routes.RESULTS_ROUTE,
	style = RouteAnimation::class,
	navArgsDelegate = ResultsScreenArgs::class
)
@Composable
fun ResultsNavRoute(
	navigator: DestinationsNavigator,
) {
	val context = LocalContext.current
	val snackBarProvider = LocalSnackBarStateProvider.current

	val viewModel = hiltViewModel<AnalysisResultsViewModel>()

	val resultsState by viewModel.resultState.collectAsStateWithLifecycle()
	val option by viewModel.analysisOption.collectAsStateWithLifecycle()

	viewModel.SideEffects { events ->
		when (events) {
			is UiEvents.ShowSnackBar -> snackBarProvider.showSnackbar(events.message)
			is UiEvents.ShowToast -> Toast.makeText(context, events.message, Toast.LENGTH_SHORT)
				.show()

			else -> {}
		}
	}

	ResultsScreen(
		resultsState = resultsState,
		analysisOption = option,
		onResultEvent = viewModel::onEvents,
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
package com.eva.scannerapp.presentation.navigation.routes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.eva.scannerapp.R
import com.eva.scannerapp.presentation.composables.UiEventsCollector
import com.eva.scannerapp.presentation.feature_gallery.ImageGalleryScreen
import com.eva.scannerapp.presentation.feature_gallery.ImageGalleryViewModel
import com.eva.scannerapp.presentation.navigation.navArgs.ResultsScreenArgs
import com.eva.scannerapp.presentation.navigation.routes.destinations.AnalysisNavRouteDestination
import com.eva.scannerapp.presentation.navigation.screen.RouteAnimation
import com.eva.scannerapp.presentation.navigation.screen.Routes
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(
	route = Routes.GALLERY_ROUTE,
	style = RouteAnimation::class,
)
@Composable
fun GalleryNavRoute(
	navigator: DestinationsNavigator
) {
	val viewModel = hiltViewModel<ImageGalleryViewModel>()

	val pages = viewModel.pagedImages.collectAsLazyPagingItems()
	val canReadImage by viewModel.canReadImage.collectAsStateWithLifecycle()

	UiEventsCollector(viewModel = viewModel)

	ImageGalleryScreen(
		canReadImage = canReadImage,
		pages = pages,
		onPermissionAllowed = viewModel::onReadPermissionChange,
		onImageSelect = {
			val fileUri = it.imageUri.toUri()
			val navArgs = ResultsScreenArgs(fileUri = fileUri)
			navigator.navigate(AnalysisNavRouteDestination(navArgs))
		},
		navigation = {
			IconButton(onClick = navigator::popBackStack) {
				Icon(
					imageVector = Icons.AutoMirrored.Filled.ArrowBack,
					contentDescription = stringResource(id = R.string.navigation_back)
				)
			}
		}
	)
}
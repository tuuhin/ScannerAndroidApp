package com.eva.scannerapp.presentation.feature_gallery

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.areNavigationBarsVisible
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.eva.scannerapp.R
import com.eva.scannerapp.domain.image.models.ImageDataModel
import com.eva.scannerapp.presentation.feature_gallery.composables.GalleryScreenTopBar
import com.eva.scannerapp.presentation.feature_gallery.composables.ImagesVerticalGrid
import com.eva.scannerapp.presentation.feature_gallery.composables.PartialAccessBanner
import com.eva.scannerapp.presentation.feature_gallery.composables.ReadPermissionsPlaceHolder
import com.eva.scannerapp.presentation.feature_gallery.state.GalleryPermissionState
import com.eva.scannerapp.presentation.feature_gallery.util.GalleryPermissionChecker
import com.eva.scannerapp.presentation.util.LocalSnackBarStateProvider
import com.eva.scannerapp.presentation.util.preview.BooleanPreviewParams
import com.eva.scannerapp.presentation.util.preview.PreviewFakes
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@OptIn(
	ExperimentalMaterial3Api::class,
	ExperimentalLayoutApi::class
)
@Composable
fun ImageGalleryScreen(
	permissionState: GalleryPermissionState,
	pages: LazyPagingItems<ImageDataModel>,
	onPermissionRecheck: (GalleryPermissionState) -> Unit,
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {},
	onImageSelect: (ImageDataModel) -> Unit,
) {

	val context = LocalContext.current
	val lifecycleOwner = LocalLifecycleOwner.current
	val snackBarHostState = LocalSnackBarStateProvider.current


	DisposableEffect(context, lifecycleOwner) {
		// this method too informs the viewmodel about the current permission state
		val observer = LifecycleEventObserver { _, event ->
			// check on start if the permission being changed
			if (event == Lifecycle.Event.ON_START) {
				val permissions = GalleryPermissionChecker.checkPermission(context)
				onPermissionRecheck(permissions)
			}
		}
		// add the observer
		lifecycleOwner.lifecycle.addObserver(observer)
		onDispose {
			// remove the observer
			lifecycleOwner.lifecycle.removeObserver(observer)
		}
	}

	val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

	Scaffold(
		topBar = {
			GalleryScreenTopBar(
				navigation = navigation,
				scrollBehavior = scrollBehavior
			)
		},
		snackbarHost = { SnackbarHost(snackBarHostState) },
		contentWindowInsets = if (WindowInsets.areNavigationBarsVisible)
			WindowInsets.navigationBars else ScaffoldDefaults.contentWindowInsets,
		modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
	) { scPadding ->
		Crossfade(
			targetState = permissionState,
			label = "Gallery permission state cross fade animation",
			animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
			modifier = Modifier.padding(scPadding)
		) { state ->
			when (state) {
				GalleryPermissionState.GRANTED,
				GalleryPermissionState.PARTIALLY_GRANTED -> ImagesVerticalGrid(
					pages = pages,
					onImageSelect = onImageSelect,
					header = {
						AnimatedVisibility(
							visible = permissionState.isPartiallyGranted,
							enter = expandVertically(),
							exit = shrinkVertically()
						) {
							PartialAccessBanner(
								onPermissionChanged = { permsState ->
									onPermissionRecheck(permsState)
									//refresh if the all read permission or partially granted
									if (permsState != GalleryPermissionState.NOT_GRANTED)
										pages.refresh()
								},
							)
						}
					},
					contentPaddingValues = PaddingValues(all = dimensionResource(id = R.dimen.scaffold_padding)),
					modifier = Modifier.fillMaxSize(),
				)

				GalleryPermissionState.NOT_GRANTED -> Box(
					modifier = Modifier
						.fillMaxSize(),
					contentAlignment = Alignment.Center
				) {
					ReadPermissionsPlaceHolder(onGalleryPerms = onPermissionRecheck)
				}

				else -> Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center
				) {
					CircularProgressIndicator(
						trackColor = MaterialTheme.colorScheme.surfaceVariant,
						color = MaterialTheme.colorScheme.secondary,
						strokeCap = StrokeCap.Round
					)
				}
			}
		}
	}
}

@PreviewLightDark
@Composable
private fun ImageGalleryScreenPreview(
	@PreviewParameter(BooleanPreviewParams::class)
	canReadImage: Boolean
) = ScannerAppTheme {
	val pages = PreviewFakes.FAKE_PAGED_DATA.collectAsLazyPagingItems()
	ImageGalleryScreen(
		permissionState = GalleryPermissionState.GRANTED,
		pages = pages,
		onPermissionRecheck = {},
		onImageSelect = {},
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Filled.ArrowBack,
				contentDescription = "Back Navigation"
			)
		}
	)
}


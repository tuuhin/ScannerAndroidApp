package com.eva.scannerapp.presentation.feature_gallery

import android.Manifest
import android.os.Build
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
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
import com.eva.scannerapp.presentation.util.LocalSnackBarStateProvider
import com.eva.scannerapp.presentation.util.preview.BooleanPreviewParams
import com.eva.scannerapp.presentation.util.preview.PreviewFakes
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ImageGalleryScreen(
	permissionState: GalleryPermissionState,
	pages: LazyPagingItems<ImageDataModel>,
	onPermissionRecheck: (GalleryPermissionState) -> Unit,
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {},
	onImageSelect: (ImageDataModel) -> Unit,
) {
	val snackBarHostState = LocalSnackBarStateProvider.current
	val lifecycleOwner = LocalLifecycleOwner.current
	val context = LocalContext.current

	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

	DisposableEffect(Unit) {
		// this method too informs the viewmodel to change its state
		val observer = LifecycleEventObserver { _, event ->
			// otherwise, check the results on lifecyle on_start
			if (event == Lifecycle.Event.ON_START) {
				// If the permission is being changed from the settings
				val fullReadAllowed = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
					ContextCompat.checkSelfPermission(
						context,
						Manifest.permission.READ_MEDIA_IMAGES
					) == PermissionChecker.PERMISSION_GRANTED
				else ContextCompat.checkSelfPermission(
					context,
					Manifest.permission.READ_EXTERNAL_STORAGE
				) == PermissionChecker.PERMISSION_GRANTED
				// check on start if there are any changes
				if (fullReadAllowed) {
					onPermissionRecheck(GalleryPermissionState.GRANTED)
					return@LifecycleEventObserver
				}

				// on android 14 need to check if we have visual media selected read access
				val partialRead = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
					ContextCompat.checkSelfPermission(
						context,
						Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
					) == PermissionChecker.PERMISSION_GRANTED
				else false
				// on start check permission
				if (partialRead) onPermissionRecheck(GalleryPermissionState.PARTIALLY_GRANTED)
			}
		}
		// add the observer
		lifecycleOwner.lifecycle.addObserver(observer)
		onDispose {
			// remove the observer
			lifecycleOwner.lifecycle.removeObserver(observer)
		}
	}

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
		modifier = modifier,
	) { scPadding ->
		Crossfade(
			targetState = permissionState.canShowResults,
			label = "Has Image Read Permissions",
			modifier = Modifier.padding(scPadding),
			animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
		) { isOk ->
			if (isOk) ImagesVerticalGrid(
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
				modifier = Modifier
					.fillMaxSize()
					.nestedScroll(scrollBehavior.nestedScrollConnection),
				contentPaddingValues = PaddingValues(all = dimensionResource(id = R.dimen.scaffold_padding))
			)
			else Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {
				ReadPermissionsPlaceHolder(onGalleryPerms = onPermissionRecheck)
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


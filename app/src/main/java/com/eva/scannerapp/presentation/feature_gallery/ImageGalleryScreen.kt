package com.eva.scannerapp.presentation.feature_gallery

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.eva.scannerapp.R
import com.eva.scannerapp.domain.image.models.ImageDataModel
import com.eva.scannerapp.presentation.feature_gallery.composables.ImagesVerticalGrid
import com.eva.scannerapp.presentation.feature_gallery.composables.ReadPermissionsPlaceHolder
import com.eva.scannerapp.presentation.util.LocalSnackBarStateProvider
import com.eva.scannerapp.presentation.util.preview.BooleanPreviewParams
import com.eva.scannerapp.presentation.util.preview.PreviewFakes
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ImageGalleryScreen(
	canReadImage: Boolean,
	pages: LazyPagingItems<ImageDataModel>,
	onPermissionAllowed: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {},
	onImageSelect: (ImageDataModel) -> Unit,
) {
	val snackBarHostState = LocalSnackBarStateProvider.current
	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(id = R.string.gallery_screen)) },
				navigationIcon = navigation,
				scrollBehavior = scrollBehavior
			)
		},
		snackbarHost = { SnackbarHost(snackBarHostState) },
		contentWindowInsets = if (WindowInsets.areNavigationBarsVisible)
			WindowInsets.navigationBars else ScaffoldDefaults.contentWindowInsets,
		modifier = modifier,
	) { scPadding ->
		Crossfade(
			targetState = canReadImage,
			label = "Has Image Read Permissions",
			modifier = Modifier.padding(scPadding),
			animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
		) { isAllowed ->
			if (isAllowed) ImagesVerticalGrid(
				pages = pages,
				onImageSelect = onImageSelect,
				modifier = Modifier
					.fillMaxSize()
					.nestedScroll(scrollBehavior.nestedScrollConnection),
				contentPaddingValues = PaddingValues(all = dimensionResource(id = R.dimen.scaffold_padding))
			)
			else Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {
				ReadPermissionsPlaceHolder(
					onPermissionChanged = onPermissionAllowed,
				)
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
		canReadImage = canReadImage,
		pages = pages,
		onPermissionAllowed = {},
		onImageSelect = {},
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Filled.ArrowBack,
				contentDescription = "Back Navigation"
			)
		}
	)
}


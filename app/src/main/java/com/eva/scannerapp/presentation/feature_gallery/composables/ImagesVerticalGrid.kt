package com.eva.scannerapp.presentation.feature_gallery.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.eva.scannerapp.R
import com.eva.scannerapp.domain.image.models.ImageDataModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImagesVerticalGrid(
	pages: LazyPagingItems<ImageDataModel>,
	onImageSelect: (ImageDataModel) -> Unit,
	modifier: Modifier = Modifier,
	columnsCount: Int = 3,
	contentPaddingValues: PaddingValues = PaddingValues(0.dp)
) {
	LazyVerticalGrid(
		columns = GridCells.Fixed(columnsCount),
		horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_4)),
		verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_4)),
		contentPadding = contentPaddingValues,
		modifier = modifier,
	) {
		items(
			count = pages.itemCount,
			key = pages.itemKey { it.id },
			contentType = pages.itemContentType { it },
		) { idx ->
			val pagedItem = pages[idx]
			pagedItem?.let { image ->
				GalleryImageLoader(
					image = image,
					modifier = Modifier
						.aspectRatio(1f)
						.clickable(role = Role.Image, onClick = { onImageSelect(pagedItem) })
						.animateItemPlacement()
				)
			}
		}
		if (pages.loadState.append is LoadState.Loading) {
			item(span = { GridItemSpan(maxLineSpan) }) {
				Text(
					text = stringResource(id = R.string.loading_more),
					textAlign = TextAlign.Center,
					modifier = Modifier.fillMaxWidth()
				)
			}
		}
	}
	// show a loading progress indicator when loading
	AnimatedVisibility(
		visible = pages.loadState.refresh is LoadState.Loading,
		enter = fadeIn(initialAlpha = .3f, animationSpec = tween(durationMillis = 600)),
		exit = fadeOut(animationSpec = tween(durationMillis = 200)),
	) {
		Box(modifier = modifier, contentAlignment = Alignment.Center) {
			CircularProgressIndicator(
				trackColor = MaterialTheme.colorScheme.surfaceVariant,
				color = MaterialTheme.colorScheme.secondary
			)
		}
	}
}

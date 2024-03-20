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
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
	header: (@Composable (LazyGridItemScope.() -> Unit))? = null,
	contentPaddingValues: PaddingValues = PaddingValues(0.dp)
) {

	val showLoadingIndicator by remember(pages.loadState.refresh) {
		derivedStateOf { pages.loadState.refresh is LoadState.Loading }
	}

	LazyVerticalGrid(
		columns = GridCells.Fixed(columnsCount),
		horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_4)),
		verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_4)),
		contentPadding = contentPaddingValues,
		modifier = modifier,
	) {
		header?.let {
			item(span = { GridItemSpan(maxLineSpan) }, content = it)
		}
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
					text = stringResource(id = R.string.paged_gallery_items_loading_next_set),
					textAlign = TextAlign.Center,
					modifier = Modifier.fillMaxWidth()
				)
			}
		}
	}
	// show a loading progress indicator when loading
	AnimatedVisibility(
		visible = showLoadingIndicator,
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

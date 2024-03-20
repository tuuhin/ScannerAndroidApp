package com.eva.scannerapp.presentation.feature_gallery.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eva.scannerapp.R
import com.eva.scannerapp.presentation.composables.ManagePermissionMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreenTopBar(
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit,
	scrollBehavior: TopAppBarScrollBehavior? = null
) {
	var isExpanded by remember { mutableStateOf(false) }

	MediumTopAppBar(
		title = { Text(text = stringResource(id = R.string.gallery_route_title)) },
		navigationIcon = navigation,
		scrollBehavior = scrollBehavior,
		actions = {
			ManagePermissionMenu(
				isExpanded = isExpanded,
				onClick = { isExpanded = !isExpanded },
				onDismissRequest = { isExpanded = false },
			)
		},
		modifier = modifier,
	)
}
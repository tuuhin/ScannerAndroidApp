package com.eva.scannerapp.presentation.feature_gallery.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.eva.scannerapp.R
import com.eva.scannerapp.domain.image.models.ImageDataModel
import com.eva.scannerapp.presentation.util.preview.PreviewFakes
import com.eva.scannerapp.ui.theme.ScannerAppTheme


@Composable
fun GalleryImageLoader(
	image: ImageDataModel,
	modifier: Modifier = Modifier,
	shape: Shape = MaterialTheme.shapes.medium,
	color: Color = MaterialTheme.colorScheme.surfaceVariant
) {
	val context = LocalContext.current
	val lifecycleOwner = LocalLifecycleOwner.current
	val view = LocalView.current

	val brushColor = Brush.verticalGradient(
		colors = listOf(
			Color.Transparent,
			Color.Transparent,
			MaterialTheme.colorScheme.surface,
		)
	)

	if (view.isInEditMode) {
		Box(
			modifier = modifier
				.defaultMinSize(minHeight = 120.dp, minWidth = 120.dp)
				.background(color = color, shape = shape),
			contentAlignment = Alignment.Center
		) {
			Image(
				painter = painterResource(id = R.drawable.ic_gallery),
				contentDescription = "A gallery Image",
				colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
				modifier = Modifier.size(40.dp)
			)
			ImageDetails(
				image = image,
				brushColor = brushColor,
				modifier = Modifier.matchParentSize()
			)
		}
		return
	}

	Box(
		modifier = modifier
			.background(color = color, shape = shape)
			.clip(shape = shape),
		contentAlignment = Alignment.Center,
	) {
		AsyncImage(
			model = ImageRequest.Builder(context)
				.data(data = image.imageUri)
				.lifecycle(owner = lifecycleOwner)
				.build(),
			contentDescription = stringResource(id = R.string.gallery_image_title, image.title),
			imageLoader = context.imageLoader,
			contentScale = ContentScale.Crop,
			filterQuality = FilterQuality.Low,
			alignment = Alignment.Center
		)

		ImageDetails(
			image = image,
			brushColor = brushColor,
			modifier = Modifier.matchParentSize()
		)
	}
}


@Composable
private fun ImageDetails(
	image: ImageDataModel,
	brushColor: Brush,
	modifier: Modifier = Modifier,
	shape: Shape = MaterialTheme.shapes.medium,
) {
	Box(
		modifier = modifier.background(brush = brushColor, shape = shape),
	) {
		Column(
			modifier = Modifier
				.align(Alignment.BottomStart)
				.padding(4.dp),
			verticalArrangement = Arrangement.spacedBy(2.dp)
		) {

			Text(
				text = buildString {
					append("Size:")
					append(image.fileSize)
				},
				maxLines = 1,
				overflow = TextOverflow.Clip,
				style = MaterialTheme.typography.labelMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
			)
			Text(
				text = buildString {
					append("Type: ")
					append(image.bucketModel.bucketName)
				},
				maxLines = 1,
				overflow = TextOverflow.Ellipsis,
				style = MaterialTheme.typography.labelSmall,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
			)
		}
	}
}


@PreviewLightDark
@Composable
private fun GalleryImageLoaderPreview() = ScannerAppTheme {
	GalleryImageLoader(image = PreviewFakes.FAKE_IMAGE_DATA_MODEL)
}
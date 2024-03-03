package com.eva.scannerapp.presentation.feature_gallery.composables

import android.graphics.Bitmap
import android.util.Size
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.R
import com.eva.scannerapp.domain.image.models.ImageDataModel
import com.eva.scannerapp.presentation.util.ext.getImageBitmap
import com.eva.scannerapp.presentation.util.preview.PreviewFakes
import com.eva.scannerapp.ui.theme.ScannerAppTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

private val throttleDuration = 800.milliseconds

@Composable
fun GalleryImageLoader(
	image: ImageDataModel,
	modifier: Modifier = Modifier,
	throttleBeforeLoading: Boolean = true,
	shape: Shape = MaterialTheme.shapes.medium,
	color: Color = MaterialTheme.colorScheme.surfaceVariant
) {
	val context = LocalContext.current
	val view = LocalView.current

	var bitmap by remember { mutableStateOf<Bitmap?>(null) }

	var containerSize by remember { mutableStateOf(IntSize.Zero) }

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

	LaunchedEffect(key1 = containerSize) {
		if (containerSize == IntSize.Zero) return@LaunchedEffect
		val size = Size(containerSize.width, containerSize.height)
		// throttle time the user may skip this image to wait for some time
		// then load the image
		if (throttleBeforeLoading) delay(duration = throttleDuration)
		// create the image bitmap when container size is set
		bitmap = image.getImageBitmap(context, size)
	}

	DisposableEffect(key1 = Unit) {
		// when the user leaves the composition to recycle the bitmap
		// which frees the native object associated with the bitmap
		onDispose {
			bitmap?.recycle()
		}
	}

	Box(
		modifier = modifier
			.onSizeChanged { size -> containerSize = size }
			.background(color = color, shape = shape)
			.clip(shape = shape),
		contentAlignment = Alignment.Center
	) {
		Crossfade(
			targetState = bitmap != null,
			label = "Loading the gallery Image",
			modifier = Modifier.matchParentSize(),
			animationSpec = tween(durationMillis = 800, easing = EaseInOut)
		) { isOk ->
			if (isOk && bitmap?.isRecycled == false)
				bitmap?.let { bitmap ->
					Image(
						bitmap = bitmap.asImageBitmap(),
						contentDescription = image.description,
						contentScale = ContentScale.Crop,
						filterQuality = FilterQuality.Medium,
						modifier = Modifier.fillMaxSize()
					)
				}
			else Box(
				modifier = modifier
					.matchParentSize()
					.background(color = color, shape = shape),
			)

		}
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
				.padding(4.dp), verticalArrangement = Arrangement.spacedBy(2.dp)
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
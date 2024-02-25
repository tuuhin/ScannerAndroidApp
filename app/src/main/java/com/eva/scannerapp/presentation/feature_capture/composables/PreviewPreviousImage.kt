package com.eva.scannerapp.presentation.feature_capture.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.DrawResult
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.eva.scannerapp.R
import com.eva.scannerapp.presentation.util.preview.PreviewLightDarkApi33
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun PreviewPreviousImage(
	imageUri: String?,
	modifier: Modifier = Modifier,
	shape: Shape = MaterialTheme.shapes.medium,
	border: BorderStroke = BorderStroke(1.5.dp, Color.White)
) {
	val context = LocalContext.current
	val view = LocalView.current
	val lifecycleOwner = LocalLifecycleOwner.current
	val localDensity = LocalDensity.current

	Box(
		modifier = modifier
			.sizeIn(
				maxWidth = dimensionResource(id = R.dimen.preview_image_max_dimen),
				maxHeight = dimensionResource(id = R.dimen.preview_image_max_dimen)
			)
	) {
		Spacer(
			modifier = Modifier
				.fillMaxSize()
				.drawWithCache {
					val paddingExtra = CornerSize(3.dp).toPx(size, localDensity)
					val radius = (shape as CornerBasedShape).topEnd.toPx(size, localDensity) +
							paddingExtra
					drawStackedBorder(
						radius = radius,
						border = border,
					)
				},
		)
		Box(
			modifier = Modifier
				.offset(3.dp, (-3).dp)
				.padding(3.dp)
				.fillMaxSize()
				.border(border = border, shape = shape)
				.clip(shape),
			contentAlignment = Alignment.Center
		) {
			if (view.isInEditMode) {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.background(MaterialTheme.colorScheme.onSurfaceVariant)
				)

			} else AsyncImage(
				model = ImageRequest.Builder(context)
					.data(imageUri)
					.lifecycle(lifecycleOwner)
					.build(),
				contentDescription = null,
				imageLoader = context.imageLoader,
				contentScale = ContentScale.Crop,
				filterQuality = FilterQuality.Low,
			)
		}
	}
}

private fun CacheDrawScope.drawStackedBorder(
	radius: Float,
	border: BorderStroke,
): DrawResult {
	val path = Path()
		.apply {
			moveTo(size.width * .7f, size.height)
			lineTo(radius, size.height)
			addArc(
				oval = Rect(
					center = Offset(radius, size.height - radius),
					radius = radius
				),
				startAngleDegrees = 90f,
				sweepAngleDegrees = 90f
			)
			lineTo(0f, size.height * .3f)
		}

	return onDrawBehind {
		drawPath(
			path = path,
			brush = border.brush,
			style = Stroke(
				width = border.width.toPx(),
				cap = StrokeCap.Round
			)
		)
	}
}


@PreviewLightDarkApi33
@Composable
fun PreviewPreviousImagePreview() = ScannerAppTheme {
	PreviewPreviousImage(null)
}
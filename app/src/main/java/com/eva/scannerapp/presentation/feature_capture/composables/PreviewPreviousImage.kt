package com.eva.scannerapp.presentation.feature_capture.composables

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.eva.scannerapp.R
import com.eva.scannerapp.domain.image.models.ImageDataModel
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun PreviewPreviousImage(
	imageModel: ImageDataModel?,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	shape: Shape = MaterialTheme.shapes.medium,
	border: BorderStroke = BorderStroke(1.dp, Color.White)
) {
	val context = LocalContext.current
	val localDensity = LocalDensity.current
	val view = LocalView.current
	val lifecycle = LocalLifecycleOwner.current

	Box(
		modifier = modifier
			.sizeIn(
				maxWidth = dimensionResource(id = R.dimen.preview_image_max_dimen),
				maxHeight = dimensionResource(id = R.dimen.preview_image_max_dimen)
			)
			.clip(shape = shape)
			.clickable(onClick = onClick, role = Role.Image)

	) {
		Spacer(
			modifier = Modifier
				.fillMaxSize()
				.drawWithCache {
					val radius = (shape as CornerBasedShape).topEnd.toPx(size, localDensity)
					drawStackedBorder(
						radius = radius,
						border = border,
					)
				},
		)
		Box(
			modifier = Modifier
				.fillMaxSize()
				.offset(2.dp, (-2).dp)
				.padding(3.dp)
				.border(border = border, shape = shape)
				.clip(shape),
			contentAlignment = Alignment.Center
		) {
			if (view.isInEditMode || imageModel?.imageUri == null) {
				Icon(
					painter = painterResource(id = R.drawable.ic_gallery),
					contentDescription = "Gallery Image",
					tint = Color.White,
					modifier = Modifier
						.defaultMinSize(minWidth = 32.dp, minHeight = 32.dp)
						.align(Alignment.Center),
				)
			} else AsyncImage(
				model = ImageRequest.Builder(context)
					.lifecycle(lifecycle)
					.data(imageModel.imageUri)
					.build(),
				contentDescription = "Gallery Image",
				imageLoader = context.imageLoader,
				contentScale = ContentScale.Crop,
				filterQuality = FilterQuality.Low,
				modifier = Modifier.matchParentSize()
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
			reset()
			moveTo(size.width * .8f, size.height)
			lineTo(radius, size.height)
			addArc(
				oval = Rect(
					center = Offset(radius, size.height - radius),
					radius = radius
				),
				startAngleDegrees = 90f,
				sweepAngleDegrees = 90f
			)
			lineTo(0f, size.height * .2f)
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


@Preview(
	uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun PreviewPreviousImagePreview() = ScannerAppTheme {
	Surface(color = MaterialTheme.colorScheme.surfaceVariant) {
		PreviewPreviousImage(imageModel = null, onClick = {})
	}
}
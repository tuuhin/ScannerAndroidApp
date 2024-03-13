package com.eva.scannerapp.presentation.feature_capture.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.DrawResult
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.R
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun CaptureBox(
	modifier: Modifier = Modifier,
	curveRadius: CornerSize = CornerSize(20.dp),
	borderStroke: BorderStroke = BorderStroke(3.dp, Color.White),
) {
	val localDensity = LocalDensity.current

	var boxSize by remember { mutableStateOf(IntSize.Zero) }

	Box(
		modifier = modifier
			.onSizeChanged { size -> boxSize = size }
			.defaultMinSize(
				minWidth = dimensionResource(id = R.dimen.capture_box_min_dim),
				minHeight = dimensionResource(R.dimen.capture_box_min_dim)
			)
			.aspectRatio(1f)
			.drawWithCache {
				// .1f as size.width *.5 gives the radius, and we want
				// to contain part of the radius thus .5 * .2 = .125 approx .1
				val extraLineLength = size.width * .1f
				drawCaptureBox(
					extraLineLength = extraLineLength,
					curveRadius = curveRadius,
					borderStroke = borderStroke,
					localDensity = localDensity
				)
			},
	)
}

private fun CacheDrawScope.drawCaptureBox(
	extraLineLength: Float,
	curveRadius: CornerSize = CornerSize(40.dp),
	borderStroke: BorderStroke = BorderStroke(2.dp, Color.White),
	localDensity: Density,
): DrawResult {
	val radius = curveRadius.toPx(size, localDensity)

	val rectSize = Offset(radius, radius)


	val curvedPathTopLeft = Path().apply {
		reset()
		moveTo(0f, radius + extraLineLength)
		lineTo(0f, radius)
		addArc(
			oval = Rect(center = rectSize, radius = radius),
			startAngleDegrees = 180f,
			sweepAngleDegrees = 90f
		)
		lineTo(radius + extraLineLength, 0f)
	}

	val curvedPathBottomLeft = Path().apply {
		reset()
		moveTo(0f, size.height - (radius + extraLineLength))
		lineTo(0f, size.height - radius)
		addArc(
			oval = Rect(center = Offset(radius, size.height - radius), radius),
			startAngleDegrees = 90f,
			sweepAngleDegrees = 90f
		)
		moveTo(radius, size.height)
		lineTo(radius + extraLineLength, size.height)
	}

	val curvedPathTopRight = Path().apply {
		reset()
		moveTo(size.width - (radius + extraLineLength), 0f)
		lineTo(size.width - radius, 0f)
		addArc(
			oval = Rect(center = Offset(size.width - radius, radius), radius = radius),
			startAngleDegrees = 270f,
			sweepAngleDegrees = 90f
		)
		lineTo(size.width, radius + extraLineLength)
	}

	val curvedPathBottomRight = Path().apply {
		reset()
		moveTo(size.width, size.height - (radius + extraLineLength))
		lineTo(size.width, size.height - radius)
		addArc(
			oval = Rect(
				center = Offset(size.width - radius, size.height - radius),
				radius = radius
			),
			startAngleDegrees = 0f,
			sweepAngleDegrees = 90f
		)
		lineTo(size.width - (radius + extraLineLength), size.height)
	}

	return onDrawBehind {
		drawPath(
			path = curvedPathBottomLeft,
			brush = borderStroke.brush,
			style = Stroke(
				width = borderStroke.width.toPx(),
				cap = StrokeCap.Round
			),
		)
		drawPath(
			path = curvedPathTopLeft,
			brush = borderStroke.brush,
			style = Stroke(
				width = borderStroke.width.toPx(),
				cap = StrokeCap.Round
			)
		)
		drawPath(
			path = curvedPathTopRight,
			brush = borderStroke.brush,
			style = Stroke(
				width = borderStroke.width.toPx(),
				cap = StrokeCap.Round
			)
		)

		drawPath(
			path = curvedPathBottomRight,
			brush = borderStroke.brush,
			style = Stroke(
				width = borderStroke.width.toPx(),
				cap = StrokeCap.Round
			)
		)
	}
}

@Preview
@Composable
fun CaptureBoxPreview() = ScannerAppTheme {
	CaptureBox()
}
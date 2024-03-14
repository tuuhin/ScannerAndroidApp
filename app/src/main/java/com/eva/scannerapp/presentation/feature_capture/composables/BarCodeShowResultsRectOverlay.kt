package com.eva.scannerapp.presentation.feature_capture.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.domain.ml.models.RecognizedModel
import com.eva.scannerapp.presentation.feature_capture.states.RecognizedItemState

@Composable
fun BarCodeShowResultsRectOverlay(
	item: RecognizedItemState,
	onTap: (RecognizedModel) -> Unit,
	modifier: Modifier = Modifier,
	borderStroke: BorderStroke = BorderStroke(2.dp, Color.White),
	cornerSize: CornerSize = CornerSize(10.dp)
) {
	val density = LocalDensity.current

	val composeRect by remember(item.firstModel) {
		derivedStateOf {
			item.boundingRect ?: Rect.Zero
		}
	}

	Box(
		modifier = modifier
			.pointerInput(composeRect) {
				detectTapGestures(
					onTap = { offset ->
						if (composeRect == Rect.Zero) return@detectTapGestures
						if (composeRect.contains(offset)) item.firstModel?.let(onTap)
					},
				)
			}
			.drawBehind {
				val radius = cornerSize.toPx(composeRect.size, density)
				val cornerRadius = CornerRadius(x = radius, y = radius)
				if (composeRect == Rect.Zero) return@drawBehind
				drawRoundRect(
					brush = borderStroke.brush,
					topLeft = composeRect.topLeft,
					size = composeRect.size,
					cornerRadius = cornerRadius,
					alpha = .2f
				)
				drawRoundRect(
					brush = borderStroke.brush,
					topLeft = composeRect.topLeft,
					size = composeRect.size,
					cornerRadius = cornerRadius,
					style = Stroke(width = with(density) { borderStroke.width.toPx() })
				)
			},
	)
}


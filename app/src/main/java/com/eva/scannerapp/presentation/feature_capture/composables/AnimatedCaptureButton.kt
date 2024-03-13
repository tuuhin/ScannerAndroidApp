package com.eva.scannerapp.presentation.feature_capture.composables

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.R
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun AnimatedCaptureButton(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	isAnimationRunning: Boolean = true,
	rippleAnimationSpec: DurationBasedAnimationSpec<Float> = tween(1200, 400, EaseInOut),
	colorAnimationSpec: DurationBasedAnimationSpec<Color> = tween(1200, 400, EaseInOut),
	interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
	color: Color = Color.White,
	shape: Shape = CircleShape,
	content: @Composable () -> Unit,
) {
	val density = LocalDensity.current

	val indication = rememberRipple()

	val infiniteTransition = rememberInfiniteTransition(label = "Infinite transition")

	val rippleColorStart = color.copy(alpha = .35f)
	val rippleColorEnd = color.copy(alpha = .02f)

	val boxSize by infiniteTransition.animateFloat(
		initialValue = with(density) { CaptureButtonDefaults.rippleBoxSizeStart.toPx() },
		targetValue = with(density) {
			if (isAnimationRunning)
				CaptureButtonDefaults.rippleBoxSize.toPx()
			else CaptureButtonDefaults.rippleBoxSizeStart.toPx()
		},
		animationSpec = infiniteRepeatable(
			animation = rippleAnimationSpec,
			repeatMode = RepeatMode.Restart
		),
		label = "Ripple Indication"
	)

	val rippleColor by infiniteTransition.animateColor(
		initialValue = if (isAnimationRunning) rippleColorStart else Color.Transparent,
		targetValue = if (isAnimationRunning) rippleColorEnd else Color.Transparent,
		animationSpec = infiniteRepeatable(
			animation = colorAnimationSpec,
			repeatMode = RepeatMode.Restart
		),
		label = "Ripple colors"
	)


	Box(
		modifier = modifier
			.defaultMinSize(
				minWidth = CaptureButtonDefaults.rippleBoxSize,
				minHeight = CaptureButtonDefaults.rippleBoxSize
			),
		contentAlignment = Alignment.Center
	) {
		// ripple container
		Box(
			modifier = Modifier
				.size(size = with(density) { boxSize.toDp() })
				.drawBehind {
					drawCircle(rippleColor)
				},
		)
		// border box
		Box(
			modifier = Modifier
				.size(size = CaptureButtonDefaults.borderBoxSize)
				.border(
					width = 2.dp,
					color = color,
					shape = shape
				)
				.background(
					color = rippleColorStart,
					shape = shape,
				)
		)
		// clickable button
		Box(
			modifier = Modifier
				.size(size = CaptureButtonDefaults.contentBoxSize)
				.clip(shape = shape)
				.background(color = color)
				.indication(
					interactionSource = interactionSource,
					indication = indication
				)
				.clickable(
					role = Role.Button,
					onClick = onClick,
					enabled = isAnimationRunning
				),
			contentAlignment = Alignment.Center
		) {
			content()
		}
	}
}

private object CaptureButtonDefaults {
	val rippleBoxSizeStart = 64.dp
	val rippleBoxSize = 120.dp
	val borderBoxSize = 85.dp
	val contentBoxSize = 70.dp
}

@Preview
@Composable
fun CaptureButtonPreview() = ScannerAppTheme {
	AnimatedCaptureButton(onClick = { }) {
		Icon(
			painter = painterResource(id = R.drawable.ic_capture),
			contentDescription = "Shutter",
			tint = Color.Black
		)
	}
}
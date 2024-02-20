package com.eva.scannerapp.domain.models

/**
 * A simple version of [android.graphics.Rect] in [android.graphics] library
 * with only some used function, not using [android.graphics.Rect] to keep the domain layer android free
 */
data class BoundingRect(
	val left: Int,
	val right: Int,
	val top: Int,
	val bottom: Int
) {
	val isEmpty: Boolean
		get() = left >= right || top >= bottom

	val isValid: Boolean
		get() = left <= right && top <= bottom

	val width: Int
		get() = right - left

	val height: Int
		get() = bottom - top

	val center: Pair<Float, Float>
		get() = (left + right) * 0.5f to (top + bottom) * 0.5f

}
package com.eva.scannerapp.data.mapper

import android.graphics.Rect
import com.eva.scannerapp.domain.ml.util.BoundingRect

fun Rect.toBoundingBox(): BoundingRect = BoundingRect(
	left = left,
	right = right,
	top = top,
	bottom = bottom
)

fun BoundingRect.toRect(): Rect = Rect(left, top, right, bottom)
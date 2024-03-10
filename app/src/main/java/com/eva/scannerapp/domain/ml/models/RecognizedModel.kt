package com.eva.scannerapp.domain.ml.models

import android.graphics.Rect

sealed class RecognizedModel(val bounding: Rect? = null)
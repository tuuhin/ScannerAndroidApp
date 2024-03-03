package com.eva.scannerapp.presentation.util

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf

val LocalSnackBarStateProvider = staticCompositionLocalOf { SnackbarHostState() }
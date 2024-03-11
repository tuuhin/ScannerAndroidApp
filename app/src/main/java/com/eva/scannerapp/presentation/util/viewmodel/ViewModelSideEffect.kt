package com.eva.scannerapp.presentation.util.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle

@Composable
inline fun <T : ScannerAppViewModel> T.SideEffects(crossinline events: suspend (UiEvents) -> Unit) {
	val lifecycleOwner = LocalLifecycleOwner.current

	LaunchedEffect(lifecycleOwner) {
		lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
			uiEvents.collect { events ->
				events(events)
			}
		}
	}

}
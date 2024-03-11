package com.eva.scannerapp.presentation.util.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.SharedFlow

abstract class ScannerAppViewModel : ViewModel() {

	abstract val uiEvents: SharedFlow<UiEvents>
}
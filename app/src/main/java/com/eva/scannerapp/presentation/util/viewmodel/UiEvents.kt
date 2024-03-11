package com.eva.scannerapp.presentation.util.viewmodel

import android.net.Uri

interface UiEvents {
	data class ShowSnackBar(val message: String) : UiEvents

	data class ShowToast(val message: String) : UiEvents

	data object NavigateBack : UiEvents

	data class NavigateToResults(val uri: Uri?) : UiEvents
}
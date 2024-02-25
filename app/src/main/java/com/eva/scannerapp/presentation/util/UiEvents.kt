package com.eva.scannerapp.presentation.util

interface UiEvents {
	data class ShowSnackBar(val message: String) : UiEvents

	data class ShowToast(val message: String) : UiEvents
}
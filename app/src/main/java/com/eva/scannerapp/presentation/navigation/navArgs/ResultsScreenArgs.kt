package com.eva.scannerapp.presentation.navigation.navArgs

import android.net.Uri

data class ResultsScreenArgs(
	val fileUri: Uri? = null,
	val fromCamera: Boolean = false,
)
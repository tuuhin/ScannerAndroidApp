package com.eva.scannerapp.util

sealed class Resource<T>(
	val content: T? = null,
	val message: String? = null,
	val throwable: Throwable? = null
) {
	data class Success<T>(val data: T, val extraMessage: String? = null) :
		Resource<T>(content = data, message = extraMessage, throwable = null)

	class Loading<T>() : Resource<T>(content = null, message = null, throwable = null)

	data class Error<T>(val error: Throwable? = null) : Resource<T>(
		content = null,
		message = error?.message,
		throwable = error
	)
}
package com.eva.scannerapp.domain.ml

sealed interface MLResource<T> {

	class Empty<T>() : MLResource<T>

	data class Success<T>(val data: List<T>) : MLResource<T>

	data class Error<T>(val error: Throwable? = null) : MLResource<T>
}
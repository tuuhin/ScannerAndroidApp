package com.eva.scannerapp.util

data class PagedResource<T>(
	val previous: Int? = null,
	val next: Int? = null,
	val results: List<T>
)
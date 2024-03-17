package com.eva.scannerapp.domain.image

import android.Manifest
import com.eva.scannerapp.domain.image.models.ImageDataModel
import com.eva.scannerapp.util.PagedResource
import kotlinx.coroutines.flow.Flow


interface ImageFileReader {

	/**
	 * Checks if [Manifest.permission.READ_MEDIA_IMAGES] on API 33
	 * and [Manifest.permission.READ_EXTERNAL_STORAGE] is allowed on Api 32 on below
	 */
	val isFullReadPermissionGranted: Boolean

	/**
	 * Checks if [Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED] is granted on API 34 and
	 * above.Its special permission, which allows specific read from all the images
	 */
	val isPartialReadAllowed: Boolean

	/**
	 * Reads the last image as a flow, it to observe for changes a ContentObserver is attached which
	 * provides the last saved images on new image capture
	 */
	val readLastImageAsFlow: Flow<ImageDataModel?>

	/**
	 * Reads images page wise, a phone can contain a lot of images thus paging is implemented
	 * thus extra images are not read until the user scrolls
	 * @param page Determines the page number
	 * @param pageSize The size of each chunk
	 * @return a paged resources for the data [PagedResource] contains number of results with next and previous
	 */
	suspend fun readImagesPaged(page: Int, pageSize: Int): PagedResource<ImageDataModel>

	/**
	 * Reads all the image
	 * @return a [List] of [ImageDataModel] ie, all the images
	 */
	suspend fun readImage(): List<ImageDataModel>

	/**
	 * Reads the number of images available
	 * @return the total number of images
	 */
	suspend fun readImageCount(): Int

	/**
	 * Reads the last saved image, this is similar to [readLastImageAsFlow] but rather than
	 * a responsive [Flow] it just provides the results.
	 */
	suspend fun readLastSavedImage(): ImageDataModel

}
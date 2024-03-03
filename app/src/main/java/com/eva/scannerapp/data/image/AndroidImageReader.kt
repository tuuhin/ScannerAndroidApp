package com.eva.scannerapp.data.image

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.Formatter
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.database.getStringOrNull
import com.eva.scannerapp.domain.image.ImageFileReader
import com.eva.scannerapp.domain.image.exceptions.FileReadPermissionNotFoundException
import com.eva.scannerapp.domain.image.exceptions.QueriedFileNotFoundException
import com.eva.scannerapp.domain.image.models.ImageBucketModel
import com.eva.scannerapp.domain.image.models.ImageDataModel
import com.eva.scannerapp.util.PagedResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class AndroidImageReader(
	private val context: Context
) : ImageFileReader {

	private val contentResolver: ContentResolver
		get() = context.applicationContext.contentResolver

	private val volumeUri: Uri
		get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
			MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
		else MediaStore.Images.Media.EXTERNAL_CONTENT_URI

	private val readPermissions: String
		get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
			Manifest.permission.READ_MEDIA_IMAGES
		else Manifest.permission.READ_EXTERNAL_STORAGE

	private val projection: Array<String>
		get() = arrayOf(
			MediaStore.Images.ImageColumns._ID,
			MediaStore.Images.ImageColumns.BUCKET_ID,
			MediaStore.Images.ImageColumns.TITLE,
			MediaStore.Images.ImageColumns.DESCRIPTION,
			MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
			MediaStore.Images.ImageColumns.SIZE,
			MediaStore.Images.ImageColumns.DATA,
			MediaStore.Images.ImageColumns.DATE_MODIFIED
		)


	override val isPermissionProvided: Boolean
		get() = ContextCompat.checkSelfPermission(
			context.applicationContext,
			readPermissions
		) == PermissionChecker.PERMISSION_GRANTED


	override suspend fun readImagesPaged(page: Int, pageSize: Int): PagedResource<ImageDataModel> {

		if (!isPermissionProvided) throw FileReadPermissionNotFoundException()

		val offset = if (page >= 0) page * pageSize else 0

		val bundle = Bundle().apply {
			//sort columns
			putStringArray(
				ContentResolver.QUERY_ARG_SORT_COLUMNS,
				arrayOf(MediaStore.Images.ImageColumns.DATE_MODIFIED)
			)
			//sort directions
			putInt(
				ContentResolver.QUERY_ARG_SORT_DIRECTION,
				ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
			)
			// limit, offset
			putInt(ContentResolver.QUERY_ARG_LIMIT, pageSize)
			putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
		}

		val sortOrder = "${MediaStore.Images.ImageColumns.DATE_MODIFIED} DESC"
		val limitOffset = " LIMIT $pageSize OFFSET $offset"

		return withContext(Dispatchers.IO) {
			val cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
				contentResolver.query(volumeUri, projection, bundle, null)
			} else contentResolver.query(
				volumeUri,
				projection,
				null,
				null,
				sortOrder + limitOffset,
				null
			)

			cursor?.use {
				val results = fetchModelsFromCursor(cursor)
				val next = if (cursor.count > 0) page + 1 else null
				PagedResource(previous = page, next = next, results = results)
			} ?: PagedResource(results = emptyList())
		}
	}

	override suspend fun readImage(): List<ImageDataModel> {
		if (!isPermissionProvided) throw FileReadPermissionNotFoundException()

		val bundle = Bundle().apply {
			//sort columns
			putStringArray(
				ContentResolver.QUERY_ARG_SORT_COLUMNS,
				arrayOf(MediaStore.Images.ImageColumns.DATE_MODIFIED)
			)
			//sort directions
			putInt(
				ContentResolver.QUERY_ARG_SORT_DIRECTION,
				ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
			)
		}

		val sortOrder = "${MediaStore.Images.ImageColumns.DATE_MODIFIED} DESC"

		return withContext(Dispatchers.IO) {
			val cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
				contentResolver.query(volumeUri, projection, bundle, null)
			} else contentResolver.query(
				volumeUri,
				projection,
				null,
				null,
				sortOrder,
				null
			)

			cursor?.use { fetchModelsFromCursor(cursor) }
				?: emptyList()
		}
	}

	override suspend fun readImageCount(): Int {
		if (!isPermissionProvided) throw FileReadPermissionNotFoundException()

		return withContext(Dispatchers.IO) {
			val cursor = contentResolver.query(
				volumeUri,
				arrayOf(MediaStore.Images.ImageColumns._ID),
				null,
				null
			)
			cursor?.use { it.count } ?: 0
		}
	}


	override suspend fun readLastSavedImage(): ImageDataModel {
		if (!isPermissionProvided) throw FileReadPermissionNotFoundException()

		val bundle = Bundle().apply {
			//sort columns
			putStringArray(
				ContentResolver.QUERY_ARG_SORT_COLUMNS,
				arrayOf(MediaStore.Images.ImageColumns.DATE_MODIFIED)
			)
			//sort directions
			putInt(
				ContentResolver.QUERY_ARG_SORT_DIRECTION,
				ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
			)
			// limit 1
			putInt(ContentResolver.QUERY_ARG_LIMIT, 1)
		}

		val sortOrder = "${MediaStore.Images.ImageColumns.DATE_MODIFIED} DESC"
		val limitOffset = " LIMIT 1"

		return withContext(Dispatchers.IO) {
			val cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
				contentResolver.query(volumeUri, projection, bundle, null)
			} else contentResolver.query(
				volumeUri,
				projection,
				null,
				null,
				sortOrder + limitOffset,
				null
			)
			cursor
				?.use { fetchModelsFromCursor(cursor).firstOrNull() }
				?: throw QueriedFileNotFoundException()
		}
	}

	private fun fetchModelsFromCursor(cursor: Cursor): List<ImageDataModel> {
		val results = mutableListOf<ImageDataModel>()
		val imageIdField = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
		val dataField = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA)
		val sizeField = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.SIZE)
		val bucketIdField = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_ID)
		val bucketNameField =
			cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME)
		val imageDescriptionField =
			cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DESCRIPTION)
		val titleField = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.TITLE)

		while (cursor.moveToNext()) {
			val data = cursor.getString(dataField)
			val file = File(data)

			if (!file.exists()) continue
			val title = cursor.getString(titleField)
			val description = cursor.getStringOrNull(imageDescriptionField)
			val imageId = cursor.getLong(imageIdField)
			val fileSize = cursor.getLong(sizeField)
			val bucketId = cursor.getLong(bucketIdField)
			val bucketName = cursor.getString(bucketNameField)
			val imageUri = ContentUris.withAppendedId(volumeUri, imageId)
			//formatted file size
			val formattedFileSize = Formatter.formatShortFileSize(context, fileSize)
			// models
			val bucket = ImageBucketModel(bucketId = bucketId, bucketName = bucketName)
			val imageModel = ImageDataModel(
				id = imageId,
				title = title,
				description = description,
				fileSize = formattedFileSize,
				bucketModel = bucket,
				imageUri = "$imageUri"
			)

			results.add(imageModel)
		}
		return results
	}
}
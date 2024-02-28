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
import com.eva.scannerapp.domain.image.exceptions.FileReadPermissionNotFoundException
import com.eva.scannerapp.domain.image.ImageFileReader
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

	private val imagesVolume: Uri
		get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) MediaStore.Images.Media.getContentUri(
			MediaStore.VOLUME_EXTERNAL
		)
		else MediaStore.Images.Media.EXTERNAL_CONTENT_URI

	private val imageReadPermissions: String
		get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES
		else Manifest.permission.READ_EXTERNAL_STORAGE

	private val imageProjection: Array<String>
		get() = arrayOf(
			MediaStore.Images.ImageColumns._ID,
			MediaStore.Images.ImageColumns.BUCKET_ID,
			MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
			MediaStore.Images.ImageColumns.SIZE,
			MediaStore.Images.ImageColumns.DATA,
			MediaStore.Images.ImageColumns.DATE_MODIFIED
		)

	private val isPermissionProvided: Boolean
		get() = ContextCompat.checkSelfPermission(
			context,
			imageReadPermissions
		) == PermissionChecker.PERMISSION_GRANTED


	override suspend fun readImages(page: Int, pageSize: Int): PagedResource<ImageDataModel> {

		val offset = ((page - 1) * pageSize).let { size ->
			if (size > 0) size else 0
		}

		val argsBundle = Bundle().apply {
			putStringArray(
				ContentResolver.QUERY_ARG_SORT_COLUMNS,
				arrayOf(MediaStore.Images.ImageColumns.DATE_MODIFIED)
			)
			putInt(
				ContentResolver.QUERY_ARG_SORT_DIRECTION,
				ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
			)
			// limit, offset
			putInt(ContentResolver.QUERY_ARG_LIMIT, pageSize)
			putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
		}

		if (!isPermissionProvided) throw FileReadPermissionNotFoundException()

		return withContext(Dispatchers.IO) {
			contentResolver.query(imagesVolume, imageProjection, argsBundle, null)
				?.use { cursor ->
					val results = fetchModelsFromCursor(cursor)
					val next = if (results.isNotEmpty()) results.size else null
					PagedResource(previous = page, next = next, results = results)
				} ?: PagedResource(results = emptyList())
		}
	}

	override suspend fun readLastSavedImage(): ImageDataModel {
		val argsBundle = Bundle().apply {
			putStringArray(
				ContentResolver.QUERY_ARG_SORT_COLUMNS,
				arrayOf(MediaStore.Images.ImageColumns.DATE_MODIFIED)
			)
			putInt(
				ContentResolver.QUERY_ARG_SORT_DIRECTION,
				ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
			)
			putInt(ContentResolver.QUERY_ARG_LIMIT, 1)
		}

		if (!isPermissionProvided) throw FileReadPermissionNotFoundException()

		return withContext(Dispatchers.IO) {
			contentResolver.query(imagesVolume, imageProjection, argsBundle, null)
				?.use { cursor -> fetchModelsFromCursor(cursor).firstOrNull() }
				?: throw QueriedFileNotFoundException()
		}
	}

	override suspend fun readBuckets(): List<ImageBucketModel> {
		val projection = arrayOf(
			MediaStore.Images.ImageColumns.BUCKET_ID,
			MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
			MediaStore.Images.ImageColumns.DATE_TAKEN
		)

		val sortOrder = "MAX(${MediaStore.Images.ImageColumns.DATE_TAKEN})"

		val bucketGroupBy = "1) GROUP BY 1, (2 "

		val results = mutableListOf<ImageBucketModel>()

		if (!isPermissionProvided) throw FileReadPermissionNotFoundException()

		return withContext(Dispatchers.IO) {
			contentResolver.query(imagesVolume, projection, bucketGroupBy, null, sortOrder)
				?.use { cursor ->
					val bucketIdField =
						cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_ID)
					val bucketNameField =
						cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME)

					while (cursor.moveToNext()) {
						val bucketId = cursor.getLong(bucketIdField)
						val bucketName = cursor.getString(bucketNameField)
						val bucket = ImageBucketModel(bucketId = bucketId, bucketName = bucketName)
						results.add(bucket)
					}
				}
			results
		}
	}

	override suspend fun readImagesFromBucket(bucketId: Long): List<ImageDataModel> {


		val argsBundle = Bundle().apply {
			putString(
				ContentResolver.QUERY_ARG_SQL_SELECTION, MediaStore.Images.ImageColumns.BUCKET_ID
			)
			putLongArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, longArrayOf(bucketId))
			putStringArray(
				ContentResolver.QUERY_ARG_SORT_COLUMNS,
				arrayOf(MediaStore.Images.ImageColumns.DATE_MODIFIED)
			)
			putInt(
				ContentResolver.QUERY_ARG_SORT_DIRECTION,
				ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
			)
		}

		val results = mutableListOf<ImageDataModel>()

		if (!isPermissionProvided) throw FileReadPermissionNotFoundException()

		return withContext(Dispatchers.IO) {
			contentResolver.query(imagesVolume, imageProjection, argsBundle, null)?.use { cursor ->
				val imageIdField =
					cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
				val titleIdField =
					cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.TITLE)
				val bucketNameField =
					cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME)

				while (cursor.moveToNext()) {
					val imageId = cursor.getLong(imageIdField)
					val title = cursor.getString(titleIdField)
					val bucketName = cursor.getString(bucketNameField)
					val imageUri = ContentUris.withAppendedId(imagesVolume, imageId)

					val bucket = ImageBucketModel(bucketId, bucketName)

					results.add(ImageDataModel(imageId, title, bucket, "$imageUri"))
				}
			}
			results
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

		while (cursor.moveToNext()) {
			val data = cursor.getString(dataField)
			val file = File(data)

			if (!file.exists()) continue

			val imageId = cursor.getLong(imageIdField)
			val fileSize = cursor.getLong(sizeField)
			val bucketId = cursor.getLong(bucketIdField)
			val bucketName = cursor.getString(bucketNameField)
			val imageUri = ContentUris.withAppendedId(imagesVolume, imageId)
			//formatted file size
			val formattedFileSize = Formatter.formatShortFileSize(context, fileSize)
			// models
			val bucket = ImageBucketModel(bucketId = bucketId, bucketName = bucketName)
			val imageModel = ImageDataModel(
				id = imageId,
				fileSize = formattedFileSize,
				bucketModel = bucket,
				imageUri = "$imageUri"
			)

			results.add(imageModel)
		}
		return results
	}
}
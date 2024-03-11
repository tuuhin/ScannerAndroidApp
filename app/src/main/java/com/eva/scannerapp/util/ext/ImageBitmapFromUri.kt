package com.eva.scannerapp.util.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.util.Size
import androidx.core.net.toUri
import com.eva.scannerapp.domain.image.models.ImageDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext


suspend fun ImageDataModel.getImageBitmap(context: Context, size: Size? = null): Bitmap? {
	val uri = imageUri.toUri()

	return withContext(Dispatchers.Default) {
		try {
			//ensuring the coroutine is active
			if (!isActive) return@withContext null

			val options = BitmapFactory.Options().apply {
				inJustDecodeBounds = true
			}
			context.contentResolver.openInputStream(uri)?.use { stream ->
				BitmapFactory.decodeStream(stream, null, options)
			}
			val imageSize = Size(options.outWidth, options.outHeight)

			val bitmapOptions = BitmapFactory.Options()
				.apply {
					// minimizes the sample size
					inSampleSize = calculateInSampleSize(actual = imageSize, to = size ?: imageSize)
				}

			// else load the image
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
				val source = ImageDecoder.createSource(context.contentResolver, uri)
				ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
					// some decoder config
					decoder.setTargetSampleSize(bitmapOptions.inSampleSize)
				}
			} else context.contentResolver.openInputStream(uri)?.use { stream ->
				BitmapFactory.decodeStream(stream, null, bitmapOptions)
			}
		} catch (e: Exception) {
			e.printStackTrace()
			null
		}
	}
}

fun calculateInSampleSize(actual: Size, to: Size): Int {
	var inSampleSize = 2
	val actualHalfHeight = actual.height / 2
	val actualHalfWidth: Int = actual.width / 2
	while (actualHalfHeight / inSampleSize >= to.height && actualHalfWidth / inSampleSize >= to.width) {
		inSampleSize *= 2
	}

	return inSampleSize
}
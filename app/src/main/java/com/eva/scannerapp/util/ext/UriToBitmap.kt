package com.eva.scannerapp.util.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

suspend fun Uri.toBitmapImage(context: Context): Bitmap? {
	if (this == Uri.EMPTY) return null
	return withContext(Dispatchers.Default) {

		try {
			// as we are going to load this only once per results, there is no necessary to sampling
			//ensuring the coroutine is active
			if (!isActive) return@withContext null
			// else load the image
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
				val source = ImageDecoder.createSource(context.contentResolver, this@toBitmapImage)
				ImageDecoder.decodeBitmap(source)
			} else MediaStore.Images.Media.getBitmap(context.contentResolver, this@toBitmapImage)
		} catch (e: Exception) {
			e.printStackTrace()
			null
		}
	}
}
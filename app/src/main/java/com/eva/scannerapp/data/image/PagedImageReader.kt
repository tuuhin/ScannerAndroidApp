package com.eva.scannerapp.data.image

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eva.scannerapp.domain.image.ImageFileReader
import com.eva.scannerapp.domain.image.models.ImageDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val PAGING_LOGGER = "IMAGE_PAGER"

class PagedImageReader(
	private val reader: ImageFileReader
) : PagingSource<Int, ImageDataModel>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageDataModel> {
		return withContext(Dispatchers.IO) {
			try {
				val loadKey = params.key ?: 0
				val pageSize = params.loadSize

				Log.d(PAGING_LOGGER, "PAGE:$loadKey PAGE_SIZE:$pageSize")
				val results = reader.readImagesPaged(page = loadKey, pageSize = pageSize)
				Log.d(PAGING_LOGGER, "RESULT_NEXT:${results.next} RESULT:${results.results.size}")

				LoadResult.Page(data = results.results, prevKey = null, nextKey = results.next)
			} catch (e: Exception) {
				e.printStackTrace()
				LoadResult.Error(e)
			}
		}
	}

	override fun getRefreshKey(state: PagingState<Int, ImageDataModel>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			val anchorPage = state.closestPageToPosition(anchorPosition)
			anchorPage?.nextKey?.minus(1)
		}
	}

	companion object {

		private val pagingConfig = PagingConfig(
			pageSize = 50,
			enablePlaceholders = false,
			initialLoadSize = 50,
		)

		fun createPager(imageReader: ImageFileReader): Pager<Int, ImageDataModel> =
			Pager(config = pagingConfig, initialKey = 0) {
				PagedImageReader(reader = imageReader)
			}
	}
}
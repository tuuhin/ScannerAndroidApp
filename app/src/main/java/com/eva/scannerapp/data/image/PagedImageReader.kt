package com.eva.scannerapp.data.image

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eva.scannerapp.domain.image.ImageFileReader
import com.eva.scannerapp.domain.models.ImageDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PagedImageReader(
	private val imageReader: ImageFileReader
) : PagingSource<Int, ImageDataModel>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageDataModel> {
		return withContext(Dispatchers.IO) {
			try {
				val loadKey = params.key ?: 0
				val pageSize = params.loadSize
				val results = imageReader.readImages(page = loadKey, pageSize = pageSize)
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
			pageSize = 20,
			enablePlaceholders = false,
			initialLoadSize = 20
		)

		fun createPager(imageReader: ImageFileReader): Pager<Int, ImageDataModel> =
			Pager(config = pagingConfig) {
				PagedImageReader(imageReader)
			}
	}
}
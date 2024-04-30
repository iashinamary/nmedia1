package ru.netology.nmedia.repository

import android.net.http.HttpException
import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.netology.nmedia.api.PostsApiService
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.error.ApiError
import java.io.IOException

class PostPagingSource(
    private val apiService: PostsApiService
): PagingSource<Long, Post>() {
    override fun getRefreshKey(state: PagingState<Long, Post>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Post> {
        try {
            val result = when (params) {
                is LoadParams.Refresh -> {
                    apiService.getLatest(params.loadSize)
                }

                is LoadParams.Prepend -> return LoadResult.Page(
                    data = emptyList(), nextKey = null, prevKey = params.key
                )

                is LoadParams.Append -> {
                    apiService.getBefore(id = params.key, count = params.loadSize)
                }
            }
            if (!result.isSuccessful) {
                throw ApiError(result.code(), result.message())
            }

            val data = result.body().orEmpty()
            return LoadResult.Page(data, prevKey = params.key, data.lastOrNull()?.id)

        } catch (e: IOException) {
            return LoadResult.Error(e)
        }
    }
}
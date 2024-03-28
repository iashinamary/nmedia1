package ru.netology.nmedia.repository


import androidx.lifecycle.LiveData
import retrofit2.http.POST
import ru.netology.nmedia.dto.Post
import java.lang.Exception

interface PostRepository {

    val data: LiveData<List<Post>>
    suspend fun getAll()

    suspend fun likeById(id: Long)

    suspend fun dislikeById(id: Long)
    suspend fun shareById(id: Long)

    suspend fun removeById(id: Long)

    suspend fun save(post: Post)

    interface Callback<T> {
        fun onSuccess(data: T){}
        fun onError(e: Exception) {}
    }
}
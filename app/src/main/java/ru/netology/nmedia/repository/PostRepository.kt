package ru.netology.nmedia.repository


import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.User
import ru.netology.nmedia.model.PhotoModel

interface PostRepository {

    val data: Flow<PagingData<Post>>
    val visibleData: Flow<List<Post>>

    fun getNewerCount(postId: Long): Flow<Int>
    suspend fun getAll()

    suspend fun likeById(id: Long)

    suspend fun dislikeById(id: Long)
    suspend fun shareById(id: Long)

    suspend fun removeById(id: Long)

    suspend fun save(post: Post)

    suspend fun readAll()

    suspend fun saveWithAttachment(post: Post,model: PhotoModel)

    suspend fun updateUser(login: String, password: String): User

    interface Callback<T> {
        fun onSuccess(data: T){}
        fun onError(e: Exception) {}
    }
}
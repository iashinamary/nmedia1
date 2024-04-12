package ru.netology.nmedia.repository


import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import retrofit2.http.POST
import ru.netology.nmedia.dto.Login
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.PhotoModel
import java.lang.Exception

interface PostRepository {

    val data: Flow<List<Post>>
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

    suspend fun updateUser(login: String, password: String)

    interface Callback<T> {
        fun onSuccess(data: T){}
        fun onError(e: Exception) {}
    }
}
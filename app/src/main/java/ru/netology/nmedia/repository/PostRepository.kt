package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import ru.netology.nmedia.dto.Post
import java.lang.Exception

interface PostRepository {
    fun getAll(): List<Post>
    fun getAllAsync(callback: GetAllCallback)
    fun likeById(id: Long) : Post
    fun dislikeById(id: Long): Post
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)

    interface GetAllCallback {
        fun onSuccess(posts: List<Post>){}
        fun onError(e: Exception) {}
    }
}
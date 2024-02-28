package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import ru.netology.nmedia.dto.Post
import java.lang.Exception

interface PostRepository {
    fun getAll(): List<Post>
    fun getAllAsync(callback: Callback<List<Post>>)

    fun likeByIdAsync(id: Long, callback: Callback<Post>)

    fun dislikeByIdAsync(id: Long, callback: Callback<Post>)
    fun shareById(id: Long)
    fun removeById(id: Long)

    fun removeByIdAsync(id: Long, callback: Callback<Post>)

    fun saveAsync(post: Post, callback: Callback<Post>)

    interface Callback<T> {
        fun onSuccess(data: T){}
        fun onError(e: Exception) {}
    }
}
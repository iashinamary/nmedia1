package ru.netology.nmedia.repository


import ru.netology.nmedia.dto.Post
import java.lang.Exception

interface PostRepository {
    fun getAllAsync(callback: Callback<List<Post>>)

    fun likeByIdAsync(id: Long, callback: Callback<Post>)

    fun dislikeByIdAsync(id: Long, callback: Callback<Post>)
    fun shareById(id: Long)

    fun removeByIdAsync(id: Long, callback: Callback<Unit>)

    fun saveAsync(post: Post, callback: Callback<Post>)

    interface Callback<T> {
        fun onSuccess(data: T){}
        fun onError(e: Exception) {}
    }
}
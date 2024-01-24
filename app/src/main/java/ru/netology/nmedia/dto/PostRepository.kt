package ru.netology.nmedia.dto

import androidx.lifecycle.LiveData

interface PostRepository {
    fun get(): LiveData<Post>
    fun like()
    fun setLike()
    fun share()
}
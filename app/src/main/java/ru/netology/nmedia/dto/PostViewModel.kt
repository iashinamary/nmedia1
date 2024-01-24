package ru.netology.nmedia.dto

import androidx.lifecycle.ViewModel

class PostViewModel: ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.get()
    fun like() = repository.like()
    fun setLike() = repository.setLike()
    fun share() = repository.share()
}
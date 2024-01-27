package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PostRepositoryInMemoryImpl
import ru.netology.nmedia.repository.PostRepository

private val empty = Post(
    id = 0,
    author = "",
    content = "",
    published = "",
    likedByMe = false,
    likes = 0,
    shares = 0,
    views = 0
)

class PostViewModel: ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun save(){
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun cancelEdit(){
        edited.value = empty
    }

    fun edit(post: Post){
        edited.value = post
    }

    fun changeContent(content: String){
        edited.value?.let {
            val text = content.trim()
            if (it.content == text){
                return
            }
            edited.value = it.copy(content = text)
        }
    }
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
}
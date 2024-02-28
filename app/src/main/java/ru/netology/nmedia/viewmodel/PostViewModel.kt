package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedState
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.utils.SingleLiveEvent
import java.io.IOException
import java.lang.Exception
import kotlin.concurrent.thread

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

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl()
    private val _state = MutableLiveData(FeedState())
    val data: LiveData<FeedState>
        get() = _state
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        load()
    }

    fun load(){
        _state.value = FeedState(loading = true)
        repository.getAllAsync(object : PostRepository.GetAllCallback {
            override fun onSuccess(posts: List<Post>) {
                _state.postValue(FeedState(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _state.postValue(FeedState(error = true))
            }
        })
    }

    fun save() {
        thread {
            edited.value?.let {
                repository.save(it)
                edited.postValue(empty)
                _postCreated.postValue(Unit)
                load()
            }
        }
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
    fun likeById(id: Long) {
        val likedByMe = _state.value
            ?.posts
            ?.find { it.id == id }
            ?.likedByMe
            ?: return
        thread {
            val newPost = if (likedByMe) {
                repository.dislikeById(id)
            } else {
                repository.likeById(id)
            }
            val newList = _state.value
                ?.posts
                .orEmpty()
                .map {
                    if(it.id == id) newPost else it
                }
            _state.postValue(_state.value?.copy(posts = newList))
        }
    }
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) {
        thread {
            val old = _state.value?.posts.orEmpty()
            _state.postValue(
                _state.value?.copy(posts = _state.value?.posts.orEmpty()
                    .filter { it.id != id }
                )
            )
            try {
                repository.removeById(id)
            } catch (e: IOException) {
                _state.postValue(_state.value?.copy(posts = old))
            }
        }
    }
}
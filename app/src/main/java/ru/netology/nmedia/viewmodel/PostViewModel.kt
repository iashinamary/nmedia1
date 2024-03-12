package ru.netology.nmedia.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.withContext
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedState
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.utils.SingleLiveEvent
import java.io.IOException
import java.lang.Exception
import kotlin.concurrent.thread
import kotlin.coroutines.coroutineContext

private val empty = Post(
    id = 0,
    author = "",
    authorAvatar = "",
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

    fun fetch(id: Long, data: Post) {
        val updatedPostList = _state.value
            ?.posts
            .orEmpty()
            .map {
                if (it.id == id) data else it
            }
        _state.postValue(_state.value?.copy(posts = updatedPostList))
    }
    fun load() {
        _state.value = FeedState(loading = true)
        repository.getAllAsync(object : PostRepository.Callback<List<Post>> {
            override fun onSuccess(data: List<Post>) {
                _state.postValue(FeedState(posts = data, empty = data.isEmpty()))
            }

            override fun onError(e: Exception) {
                _state.postValue(FeedState(error = true))
            }
        })
    }

    fun save() {

        edited.value?.let {
            repository.saveAsync(it, object : PostRepository.Callback<Post> {
                override fun onSuccess(data: Post) {
                    _postCreated.postValue(Unit)
                }

                override fun onError(e: Exception) {

                }
            })
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content == text) {
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

        val likeCallback = object : PostRepository.Callback<Post> {
            override fun onSuccess(data: Post) {
                fetch(id, data)
            }
        }
        if (likedByMe) {
            repository.dislikeByIdAsync(id, likeCallback)
        } else {
            repository.likeByIdAsync(id, likeCallback)
        }
    }

    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) {
        val old = _state.value?.posts.orEmpty()
        _state.postValue(
            _state.value?.copy(
                posts = _state.value?.posts.orEmpty()
                    .filter { it.id != id }
            )
        )

        repository.removeByIdAsync(id, object : PostRepository.Callback<Unit> {

            override fun onSuccess(data: Unit) {
                val updatedPostList = _state.value
                    ?.posts
                    .orEmpty()
                    .map {
                        if (it.id == id) data else it
                    }
                _state.postValue(_state.value?.copy(posts = updatedPostList as List<Post>))
            }

            override fun onError(e: Exception) {
                _state.postValue(_state.value?.copy(posts = old))
            }
        })
    }
}
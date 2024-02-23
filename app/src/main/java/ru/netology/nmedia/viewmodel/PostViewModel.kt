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
        thread {
            _state.postValue(FeedState(loading = true))
            try {
                val posts = repository.getAll()
                FeedState(posts = posts, empty = posts.isEmpty())
            } catch (e: IOException) {
                FeedState(error = true)
            }.also(_state::postValue)
        }
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
        thread {
            repository.likeById(id)
        }
        load()
    }
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
}

package ru.netology.nmedia.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.model.PhotoModel
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.utils.SingleLiveEvent
import java.io.File
import javax.inject.Inject
import kotlin.Exception

private val empty = Post(
    id = 0,
    authorId = 0,
    author = "",
    authorAvatar = "",
    content = "",
    published = "",
    likedByMe = false,
    likes = 0,
    shares = 0,
    views = 0
)

@HiltViewModel
@ExperimentalCoroutinesApi
class PostViewModel @Inject constructor(
    private val repository: PostRepository,
    appAuth: AppAuth
) : ViewModel() {



    val data: LiveData<FeedModel> = appAuth
        .authStateFlow
        .flatMapLatest { auth ->
            repository.data.map { posts ->
                FeedModel(
                    posts.map { it.copy(ownedByMe = auth.id == it.authorId) },
                    posts.isEmpty()
                )
            }
        }
        .asLiveData(Dispatchers.Default)


    val newerCount: LiveData<Int> = data.switchMap {
        val firstId = it.posts.firstOrNull()?.id ?: 0L

        repository.getNewerCount(firstId)
            .catch { e -> e.printStackTrace() }
            .asLiveData(Dispatchers.Default)
    }

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val _photo = MutableLiveData<PhotoModel?>()
    val photo: LiveData<PhotoModel?>
        get() = _photo


    private val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(errorLoading = true)
        }

    }

    fun refreshPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(refreshing = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }


    fun save() {

        edited.value?.let { post ->
            viewModelScope.launch {
                try {
                    photo.value?.let {
                        repository.saveWithAttachment(post, it)
                    } ?: repository.save(post)
                    _postCreated.value = Unit
                    edited.value = empty
                    _dataState.value = FeedModelState()
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
            }
        }
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
        val likedByMe = data.value
            ?.posts
            ?.find { it.id == id }
            ?.likedByMe
            ?: return

        viewModelScope.launch {
            try {
                if (likedByMe) {
                    repository.dislikeById(id)
                } else {
                    repository.likeById(id)
                }
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    suspend fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) {
        val oldPost = data.value?.posts.orEmpty()
        val postToRemove = oldPost.filter { it.id != id }
        viewModelScope.launch {
            try {
                if (postToRemove.isEmpty()) {
                    _dataState.value = FeedModelState(error = true)
                } else {
                    repository.removeById(id)
                    val updatedPostList = data.value
                        ?.posts
                        .orEmpty()
                        .map {
                            if (it.id == id) data else it
                        }
                    data.value?.copy(posts = updatedPostList as List<Post>)
                }
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)

            }
        }
    }

    fun readAll() {
        viewModelScope.launch {
            repository.readAll()
        }
    }

    fun setPhoto(uri: Uri, file: File) {
        _photo.value = PhotoModel(uri, file)
    }

    fun clearPhoto() {
        _photo.value = null
    }
}
package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Login
import ru.netology.nmedia.dto.User
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl

class AuthFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl(
        AppDb.getInstance(context = application).postDao()
    )

    private val _login = MutableLiveData<Login>()
    val login: LiveData<Login>
        get() = _login
    fun auth(userParams: Login){

        login.value.let {
            viewModelScope.launch {
                try {
                    repository.updateUser(login.value?.username ?: "",
                        login.value?.password ?: "")
                } catch (e: Exception){
                    _login.value = Login(error = true)
                }
            }
        }
    }
}
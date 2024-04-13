package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.AuthResult
import ru.netology.nmedia.dto.Login
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl

class AuthFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl(
        AppDb.getInstance(context = application).postDao()
    )

    private val login = MutableLiveData<Login>()
    private val _authResult: MutableLiveData<AuthResult> = MutableLiveData()
    val authResult: LiveData<AuthResult> = _authResult

    fun auth(userParams: Login) {
        viewModelScope.launch {
            try {
                val user = repository.updateUser(
                    userParams.username,
                    userParams.password
                )
                login.postValue(Login(userParams.username, userParams.password))
                AppAuth.getInstance().setAuth(user.id, user.token)
                _authResult.postValue(AuthResult(success = true))
            } catch (e: Exception) {
                _authResult.postValue(AuthResult(success = false))
            }
        }
    }
}
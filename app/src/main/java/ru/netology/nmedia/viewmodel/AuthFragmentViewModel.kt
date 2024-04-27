package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.AuthResult
import ru.netology.nmedia.dto.Login
import ru.netology.nmedia.repository.PostRepository

class AuthFragmentViewModel(
    private val repository: PostRepository,
    private val appAuth: AppAuth
) : ViewModel() {


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
                appAuth.setAuth(user.id, user.token)
                _authResult.postValue(AuthResult(success = true))
            } catch (e: Exception) {
                _authResult.postValue(AuthResult(success = false))
            }
        }
    }
}
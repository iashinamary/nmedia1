package ru.netology.nmedia.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostsApiService
import ru.netology.nmedia.dto.Post


class PostRepositoryImpl: PostRepository {

    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
        PostsApiService.service.getAll()
            .enqueue(object : Callback<List<Post>>{
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }

                    val body = response.body() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(body)
                    } catch (e: Exception){
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callback.onError(Exception(t))
                }

            })
    }

    override fun likeByIdAsync(id: Long, callback: PostRepository.Callback<Post>) {

      PostsApiService.service.likeById(id)
            .enqueue(object : Callback<Post>{
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }

                    val body = response.body() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(body)
                    } catch (e: Exception){
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }

            })
    }


    override fun dislikeByIdAsync(id: Long, callback: PostRepository.Callback<Post>) {

        PostsApiService.service.dislikeById(id)
            .enqueue(object : Callback<Post>{
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }

                    val body = response.body() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(body)
                    } catch (e: Exception){
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }

            })
    }

    override fun shareById(id: Long) {

    }

    override fun saveAsync(post: Post, callback: PostRepository.Callback<Post>) {


        PostsApiService.service.save(post)
            .enqueue(object : Callback<Post>{
                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }

                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    val body = response.body() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(body)
                    } catch (e: Exception){
                        callback.onError(e)
                    }
                }

            })
    }


    override fun removeByIdAsync(id: Long, callback: PostRepository.Callback<Unit>) {
        PostsApiService.service.removeById(id)
            .enqueue(object : Callback<Unit>{
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    val body = response.body() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(body)
                    } catch (e: Exception){
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(Exception(t))
                }

            })

    }


}
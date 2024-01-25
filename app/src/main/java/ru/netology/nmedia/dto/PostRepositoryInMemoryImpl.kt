package ru.netology.nmedia.dto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.repository.PostRepository

class PostRepositoryInMemoryImpl: PostRepository {
    private var posts = listOf(
        Post(
            id = 2,
            author = "Нетология. Университет интернет-профессий",
            content = "Знаний хватит на всех: на следующей неделе разбираемся с разработкой мобильных приложений, учимся рассказывать истории и составлять PR-стратегию прямо на бесплатных занятиях" ,
            published = "18 сентября в 10:12",
            likedByMe = false,
            likes = 199,
            shares = 8_999,
            views = 10_236_568
        ),
        Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        published = "21 мая в 18:36",
        likedByMe = false,
        likes = 999,
        shares = 10_999,
        views = 10_236_568
        ),
    )

    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) {
                it
            } else {
                val updatedPost = it.copy(likedByMe = !it.likedByMe)
                if (updatedPost.likedByMe) {
                    updatedPost.copy(likes = ++updatedPost.likes)
                } else {
                    updatedPost.copy(likes = --updatedPost.likes)
                }
            }
        }
        data.value = posts
    }



    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(shares = ++it.shares)
        }
        data.value = posts
    }


}
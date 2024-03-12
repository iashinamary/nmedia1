package ru.netology.nmedia.dto

import android.widget.ImageView

data class Post(
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    var likes: Int,
    var shares: Int,
    var views: Int)

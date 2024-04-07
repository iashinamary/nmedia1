package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    var likes: Int,
    var shares: Int,
    var views: Int,
    var attachment: Attachment? = null,
)

data class Attachment(
    val url: String,
    val type: AttachmentType,
)
enum class AttachmentType {
    IMAGE
}


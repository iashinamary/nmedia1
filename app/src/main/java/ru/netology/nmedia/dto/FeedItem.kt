package ru.netology.nmedia.dto

sealed class FeedItem{
    abstract val id: Long
}

data class Post(
    override val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    var likes: Int,
    var shares: Int,
    var views: Int,
    var attachment: Attachment? = null,
    val ownedByMe: Boolean = false,
) : FeedItem()

data class Ad(
    override val id: Long,
    val image: String,
) : FeedItem()

data class Attachment(
    val url: String,
    val type: AttachmentType,
)
enum class AttachmentType {
    IMAGE
}


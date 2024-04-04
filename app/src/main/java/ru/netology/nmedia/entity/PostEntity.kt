package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    var likes: Int = 0,
    var shares: Int = 0,
    var views: Int = 0,
    val hidden: Boolean = false,
) {

    fun toDto() =
        Post(id, author, authorAvatar, content, published, likedByMe, likes, shares, views)

    companion object {
        fun fromDto(dto: Post, hidden: Boolean = false) =
            PostEntity(
                dto.id,
                dto.author,
                dto.authorAvatar,
                dto.content,
                dto.published,
                dto.likedByMe,
                dto.likes,
                dto.shares,
                dto.views,
                hidden
            )

    }
}

fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(hidden: Boolean = false): List<PostEntity> = map { PostEntity.fromDto(it, hidden) }
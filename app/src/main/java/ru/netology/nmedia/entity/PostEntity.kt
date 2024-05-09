package ru.netology.nmedia.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: Long,
    val likedByMe: Boolean,
    var likes: Int = 0,
    var shares: Int = 0,
    var views: Int = 0,
    @Embedded
    val attachment: Attachment?,
    val ownedByMe: Boolean = false,
) {

    fun toDto() =
        Post(
            id = id,
            authorId = authorId,
            author = author,
            authorAvatar = authorAvatar,
            content = content,
            published = published,
            likedByMe = likedByMe,
            likes = likes,
            shares = shares,
            views = views,
            attachment = attachment,
            ownedByMe = ownedByMe
        )

    companion object {
        fun fromDto(dto: Post, hidden: Boolean = false) =
            PostEntity(
                id = dto.id,
                authorId = dto.authorId,
                author = dto.author,
                authorAvatar = dto.authorAvatar,
                content = dto.content,
                published = dto.published,
                likedByMe = dto.likedByMe,
                likes = dto.likes,
                shares = dto.shares,
                views = dto.views,
                attachment = dto.attachment,
                ownedByMe = dto.ownedByMe
            )

    }
}

fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(hidden: Boolean = false): List<PostEntity> =
    map { PostEntity.fromDto(it, hidden) }
package ru.netology.nmedia.adapter

import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Counter
import ru.netology.nmedia.OnInteractionListener
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostLayoutBinding
import ru.netology.nmedia.dto.Post

class PostViewHolder(
    private val binding: CardPostLayoutBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post){
            binding.apply {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                like.text = Counter.reduce(post.likes)
                share.text = Counter.reduce(post.shares)
                viewCount.text = Counter.reduce(post.views)

                like.isChecked = post.likedByMe

                like.setOnClickListener {
                    onInteractionListener.onLike(post)
                }
                share.setOnClickListener {
                    onInteractionListener.onShare(post)
                }

                root.setOnClickListener{
                    onInteractionListener.onDetailsClick(post)
                }
                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { item ->
                            when(item.itemId){
                                R.id.remove -> {
                                    onInteractionListener.onRemove(post)
                                    true
                                }
                                R.id.edit -> {
                                    onInteractionListener.onEdit(post)
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }
            }
        }

}
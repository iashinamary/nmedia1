package ru.netology.nmedia.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.databinding.CardAdLayoutBinding
import ru.netology.nmedia.dto.Ad
import ru.netology.nmedia.utils.load

class AdViewHolder(
    private val binding: CardAdLayoutBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(ad: Ad){
        binding.apply {
            imageAd.load("${BuildConfig.BASE_URL}/media/${ad.image}")
        }
    }

}
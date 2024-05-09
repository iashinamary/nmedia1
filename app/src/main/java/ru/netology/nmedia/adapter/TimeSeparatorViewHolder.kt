package ru.netology.nmedia.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.TimeSeparatorLayoutBinding
import ru.netology.nmedia.dto.TimeSeparator

class TimeSeparatorViewHolder(
    private val binding: TimeSeparatorLayoutBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(timeSeparator: TimeSeparator){
        binding.apply {
            timeSeparation.text = timeSeparator.text
        }
    }

}
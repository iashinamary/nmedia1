package ru.netology.nmedia.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop

fun ImageView.load(url: String, vararg transforms: BitmapTransformation = emptyArray()) {
    Glide.with(this)
        .load(url)
        .timeout(10_000)
        .transform(*transforms)
        .into(this)
}

fun ImageView.loadAndCrop(url: String, vararg transforms: BitmapTransformation = emptyArray()) {
    load(url, CircleCrop(), *transforms)
}
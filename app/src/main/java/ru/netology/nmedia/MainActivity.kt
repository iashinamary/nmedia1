package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            likes = 999,
            shares = 10_999,
            views = 10_236_568
        )

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeCount.text = reduce(post.likes)
            shareCount.text = reduce(post.shares)
            viewCount.text = reduce(post.views)
            if (post.likedByMe) {
                like?.setImageResource(R.drawable.ic_baseline_favorite_24)
            }

            like?.setOnClickListener {
                post.likedByMe = !post.likedByMe
                like.setImageResource(
                    if (post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                )
                if (post.likedByMe) post.likes++ else post.likes--
                likeCount.text = reduce(post.likes)

            }

            share?.setOnClickListener {
                shareCount.text = reduce((post.shares))
            }
        }

    }

    fun reduce(count: Int): String =
        when (count) {
            in 0..999 -> count.toString()
            in 1000..9_999 -> {
                val number = BigDecimal(count)
                val result = number.divide(BigDecimal(1000), 1, RoundingMode.FLOOR)
                result.toString() + "K"
            }
            in 10_000..999_999 -> {
                val number = BigDecimal(count)
                val result = number.divide(BigDecimal(1000), 0, RoundingMode.FLOOR)
                result.toString() + "K"
            }
            else -> {
                val number = BigDecimal(count)
                val result = number.divide(BigDecimal(1000000), 1, RoundingMode.FLOOR)
                result.toString() + "M"
            }
        }
}
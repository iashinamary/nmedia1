package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostAdapter( object : OnInteractionListener {
            override fun onEdit(post: Post){
                viewModel.edit(post)
            }

            override fun onLike(post: Post){
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post){
                viewModel.shareById(post.id)
            }

            override fun onRemove(post: Post){
                viewModel.removeById(post.id)
            }

        }
        )
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.cancelEdit.setOnClickListener {
            viewModel.cancelEdit()
            binding.newContent.setText("")
        }

        viewModel.edited.observe(this){ post ->
            if(post.id == 0L){
                binding.editGroup.isGone = true
                return@observe
            }

            binding.preview.text = post.content

            binding.editGroup.isVisible = true

            with(binding.newContent){
                requestFocus()
                setText(post.content)
            }
        }

        binding.save.setOnClickListener {
            with(binding.newContent){
                if(text.isNullOrBlank()){
                    Toast.makeText(
                        this@MainActivity,
                        "Content can't be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.changeContent(text.toString())
                viewModel.save()

                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
            }
        }

    }
}
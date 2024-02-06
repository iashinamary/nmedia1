package ru.netology.nmedia.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.NewPostResultContract
import ru.netology.nmedia.OnInteractionListener
import ru.netology.nmedia.R
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
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                intent.let {
                    if (it.action != Intent.ACTION_SEND){
                        return@let
                    }

                    val text = it.getStringExtra(Intent.EXTRA_TEXT)
                    if (text.isNullOrBlank()) {
                        Snackbar.make(binding.root, R.string.error_empty_content, LENGTH_INDEFINITE)
                            .setAction(android.R.string.ok){
                                finish()
                            }
                            .show()
                        return@let
                    }
                }

                val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
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
        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
            result ?: return@registerForActivityResult
            viewModel.changeContent(result)
            viewModel.save()
        }

        binding.fab.setOnClickListener {
            newPostLauncher.launch()
        }
    }
}
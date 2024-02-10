package ru.netology.nmedia.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostViewHolder
import ru.netology.nmedia.databinding.CardPostLayoutBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.fragments.NewPostFragment.Companion.textArg
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.OnInteractionListener as OnInteractionListener1

const val ARG_POST_ID = "postId"

class PostDetailsFragment: Fragment() {

    private var postId: Long? = null

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postId = it.getLong(ARG_POST_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = CardPostLayoutBinding.inflate(inflater,container, false)

        val viewHolder = PostViewHolder(binding, object: OnInteractionListener1 {

            override fun onEdit(post: Post){
                viewModel.edit(post)
                findNavController().navigate(
                    R.id.action_postDetailsFragment_to_newPostFragment, Bundle().apply {
                        textArg = post.content
                    }
                )
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

                val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

            override fun onRemove(post: Post){
                viewModel.removeById(post.id)
            }


        })

        viewModel.data.observe(viewLifecycleOwner){ posts ->
            val post = posts.find { it.id== postId }

            if (post == null) {
                findNavController().navigateUp()
                return@observe
            }

            viewHolder.bind(post)
        }

        return binding.root
    }
}
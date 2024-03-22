package ru.netology.nmedia.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.OnInteractionListener
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.FeedFragmentLayoutBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.fragments.NewPostFragment.Companion.textArg
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FeedFragmentLayoutBinding.inflate(
            inflater,
            container,
            false
        )
        val adapter = PostAdapter( object : OnInteractionListener {
            override fun onEdit(post: Post){
                viewModel.edit(post)
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment, Bundle().apply {
                    textArg = post.content
                })
//
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

                viewModel.shareById(post.id)
            }

            override fun onRemove(post: Post){
                viewModel.removeById(post.id)
            }

        }
        )
        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.posts)
            binding.progress.isVisible = state.loading
            binding.errorGroup.isVisible = state.error_loading
            binding.emptyText.isVisible = state.empty
            if(state.unsaved) {
                Toast.makeText(context, getString(R.string.unsuccessfull_save), Toast.LENGTH_SHORT)
                    .show()
            }
            if(state.error){
                Toast.makeText(context, getString(R.string.error_loading), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.retryButton.setOnClickListener {
            viewModel.load()
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        binding.swiperefresh.setOnRefreshListener {
            viewModel.load()
            binding.swiperefresh.isRefreshing = false
        }

        return binding.root
    }
}
package ru.netology.nmedia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.PictureFragmentLayoutBinding
import ru.netology.nmedia.utils.load
import ru.netology.nmedia.viewmodel.PostViewModel

const val ARG_URL = "postId"
class PictureFragment : Fragment() {

    private var url: String? = null

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString(ARG_URL)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = PictureFragmentLayoutBinding.inflate(
            inflater,
            container,
            false
        )

        binding.picture.load("http://10.0.2.2:9999/media/$url")

        binding.back.setOnClickListener{
            findNavController().navigateUp()
        }

        return binding.root
    }
}
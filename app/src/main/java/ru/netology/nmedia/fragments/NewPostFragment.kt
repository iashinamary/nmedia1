package ru.netology.nmedia.fragments


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.MenuProvider
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.NewPostFragmentLayoutBinding
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.utils.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by activityViewModels()

    private val photoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode != Activity.RESULT_OK){
            return@registerForActivityResult
        }

        val uri = requireNotNull(it.data?.data)
        val file = uri.toFile()

        viewModel.setPhoto(uri, file)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = NewPostFragmentLayoutBinding.inflate(
            inflater,
            container,
            false
        )

        arguments?.textArg
            ?.let(binding.edit::setText)


        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.save_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                    when (menuItem.itemId){
                        R.id.save -> {
                            viewModel.changeContent(binding.edit.text.toString())
                            viewModel.save()
                            AndroidUtils.hideKeyboard(requireView())
                            findNavController().navigateUp()
                            true
                        }
                        else -> false
                    }

            }, viewLifecycleOwner
        )

        viewModel.photo.observe(viewLifecycleOwner){photo ->
            if (photo == null){
                binding.photoContainer.isGone = true
                return@observe
            }
            binding.photoContainer.isVisible = true
            binding.photo.setImageURI(photo.uri)
        }

        binding.gallery.setOnClickListener{
            ImagePicker.Builder(this)
                .galleryOnly()
                .crop()
                .maxResultSize(2048, 2048)
                .createIntent(photoLauncher::launch)
        }
        binding.takePhoto.setOnClickListener{
            ImagePicker.Builder(this)
                .cameraOnly()
                .crop()
                .maxResultSize(2048, 2048)
                .createIntent(photoLauncher::launch)
        }

        binding.removeAttachment.setOnClickListener {
            viewModel.clearPhoto()
        }

        viewModel.postCreated.observe(viewLifecycleOwner){
            viewModel.loadPosts()
            findNavController().navigateUp()
        }

        return binding.root
    }
}
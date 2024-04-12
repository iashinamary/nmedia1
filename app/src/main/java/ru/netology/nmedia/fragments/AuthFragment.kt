package ru.netology.nmedia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.AuthFragmentLayoutBinding
import ru.netology.nmedia.dto.Login
import ru.netology.nmedia.viewmodel.AuthFragmentViewModel

class AuthFragment : Fragment() {

    private val viewModel: AuthFragmentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = AuthFragmentLayoutBinding.inflate(
            inflater,
            container,
            false
        )

        binding.saveLogin.setOnClickListener {
            viewModel.auth(
                Login(
                    binding.login.text.toString(),
                    binding.password.text.toString()
                )
            )
            findNavController().navigateUp()
        }

        return binding.root
    }
}
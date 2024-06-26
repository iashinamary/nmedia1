package ru.netology.nmedia.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.databinding.AuthFragmentLayoutBinding
import ru.netology.nmedia.dto.Login
import ru.netology.nmedia.viewmodel.AuthFragmentViewModel

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private val viewModel: AuthFragmentViewModel by activityViewModels()
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

            viewModel.authResult.observe(viewLifecycleOwner, Observer { result ->
                if (result.success){
                    findNavController().navigateUp()
                } else {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Ошибка")
                        .setMessage("Ошибка аутентификации")
                        .setPositiveButton("@string/auth_error") { dialog, id ->
                            viewModel.auth(
                                Login(
                                    binding.login.text.toString(),
                                    binding.password.text.toString()
                                )
                            )
                        }
                    builder.create()
                }
            })


        }

        return binding.root
    }
}
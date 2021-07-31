package com.company.app.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.company.app.App
import com.company.app.R
import com.company.app.User
import com.google.android.material.textfield.TextInputEditText

class LoginFragment : Fragment() {

//    private val loginViewModel: LoginViewModel by viewModels {
//        LoginViewModelFactory((activity?.application as App).repository)
//    }
    private lateinit var loginInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var signInButton: Button

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        loginInput = root.findViewById(R.id.tiet_sign_in_enter_login)
        passwordInput = root.findViewById(R.id.tiet_sign_in_enter_password)
        signInButton = root.findViewById(R.id.btn_sign_in)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signInButton.setOnClickListener {
//            loginViewModel.register("Grozehich", "123456")
//            findNavController().navigate(R.id.action_navigation_login_to_navigation_registration)
        }
    }
}

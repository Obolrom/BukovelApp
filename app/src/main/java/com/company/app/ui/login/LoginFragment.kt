package com.company.app.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.company.app.R
import com.google.android.material.textfield.TextInputEditText

class LoginFragment : Fragment() {
    interface LoginSignUpLink {
        fun onLinkListener()
    }

    private lateinit var callback: LoginSignUpLink
    private lateinit var loginInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var signInButton: Button

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.callback = (context as? LoginSignUpLink)!!
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        loginViewModel =
                ViewModelProvider(this).get(LoginViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        loginInput = root.findViewById(R.id.tiet_sign_in_enter_login)
        passwordInput = root.findViewById(R.id.tiet_sign_in_enter_password)
        signInButton = root.findViewById(R.id.btn_sign_in)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        signInButton.setOnClickListener {
            Toast.makeText(context, "sign in button", Toast.LENGTH_SHORT).show()
            callback.onLinkListener()
        }
    }
}

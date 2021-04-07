package com.company.app.ui.registration

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.company.app.R
import com.google.android.material.textfield.TextInputEditText

class RegistrationFragment : Fragment() {

    private lateinit var root: View
    private lateinit var loginInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var approvePasswordInput: TextInputEditText
    private lateinit var signUpButton: Button

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_registration, container, false)
        loginInput = root.findViewById(R.id.tiet_sign_up_enter_login)
        passwordInput = root.findViewById(R.id.tiet_sign_up_enter_password)
        approvePasswordInput = root.findViewById(R.id.tiet_sign_up_enter_password_again)
        signUpButton = root.findViewById(R.id.btn_sign_up)

        return root
    }
}
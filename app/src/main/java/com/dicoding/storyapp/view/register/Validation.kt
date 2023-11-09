package com.dicoding.storyapp.view.register

import android.text.Editable
import android.text.TextWatcher
import com.dicoding.storyapp.databinding.ActivityRegisterBinding

object Validation {
    fun nameValidation(p: ActivityRegisterBinding?) {
        val nameEditText = p?.nameEditText
        nameEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                val name = s.toString().trim()
                if (name.isEmpty()) {
                    p.nameEditTextLayout.error = "Nama tidak boleh kosong"
                } else {
                    p.nameEditTextLayout.error = null
                }
            }
        })
    }

    fun buttonValidation(p: ActivityRegisterBinding?) {
        val nameEditTextLayout = p?.nameEditTextLayout
        val emailEditTextLayout = p?.emailEditTextLayout
        val passwordEditTextLayout = p?.passwordEditTextLayout
        val registerButton = p?.registerButton

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                val name = nameEditTextLayout?.editText?.text.toString().trim()
                val email = emailEditTextLayout?.editText?.text.toString().trim()
                val password = passwordEditTextLayout?.editText?.text.toString().trim()

                val isNotEmpty = name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()

                registerButton?.isEnabled = isNotEmpty
            }
        }

        nameEditTextLayout?.editText?.addTextChangedListener(textWatcher)
        emailEditTextLayout?.editText?.addTextChangedListener(textWatcher)
        passwordEditTextLayout?.editText?.addTextChangedListener(textWatcher)
    }
}
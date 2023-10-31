package com.dicoding.storyapp.view.login


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.ViewModelFactory
import com.dicoding.storyapp.data.model.LoginRequest
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.view.main.MainActivity


class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupView()
        playAnimation()

        binding?.loginButton?.setOnClickListener {
            viewModel.response.observe(this) {
                Log.d("res", it.message)
            }
            setupAction()
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
        buttonValidation()
    }

    private fun setupAction() {
        val email = binding?.emailEditText?.text.toString()
        val password = binding?.passwordEditText?.text.toString()
        val user = LoginRequest(email, password)

        viewModel.postLogin(user)
        viewModel.response.observe(this) {
            if (!it.error) {
                showToast(it.message)

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                showToast(it.message)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.loadingBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun buttonValidation() {
        val emailEditTextLayout = binding?.emailEditTextLayout
        val passwordEditTextLayout = binding?.passwordEditTextLayout
        val loginButton = binding?.loginButton

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                val email = emailEditTextLayout?.editText?.text.toString().trim()
                val password = passwordEditTextLayout?.editText?.text.toString().trim()

                val isNotEmpty = email.isNotEmpty() && password.isNotEmpty()

                loginButton?.isEnabled = isNotEmpty
            }
        }

        emailEditTextLayout?.editText?.addTextChangedListener(textWatcher)
        passwordEditTextLayout?.editText?.addTextChangedListener(textWatcher)
    }

    private fun playAnimation() {

        val viewsToAnimate = listOf(
            binding?.titleTextView,
            binding?.messageTextView,
            binding?.emailTextView,
            binding?.emailEditTextLayout,
            binding?.passwordTextView,
            binding?.passwordEditTextLayout,
            binding?.loginButton
        )

        val animatorSet = AnimatorSet()
        val animations = viewsToAnimate
            .filterNotNull()
            .map { view ->
                ObjectAnimator.ofFloat(view, View.ALPHA, 1f).setDuration(100)
            }

        animatorSet.playSequentially(*animations.toTypedArray())
        animatorSet.startDelay = 50
        animatorSet.start()
    }
}

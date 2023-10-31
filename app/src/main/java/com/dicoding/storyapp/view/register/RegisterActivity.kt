package com.dicoding.storyapp.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.ViewModelFactory
import com.dicoding.storyapp.data.model.RegisterRequest
import com.dicoding.storyapp.databinding.ActivityRegisterBinding
import com.dicoding.storyapp.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupView()
        playAnimation()

        binding?.registerButton?.setOnClickListener {
            setupAction()
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.response.observe(this) {
            if (!it.error) {
                showToast(it.message)
                showLoading(false)
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                showToast(it.message)
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        Validation.nameValidation(binding)
        Validation.buttonValidation(binding)
    }



    private fun setupAction() {
        val name = binding?.nameEditText?.text.toString().trim()
        val email = binding?.emailEditText?.text.toString().trim()
        val password = binding?.passwordEditText?.text.toString().trim()

        val userData = RegisterRequest(name, email, password)
        viewModel.postRegister(userData)
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.loadingBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        val animators = listOf(
            ObjectAnimator.ofFloat(binding?.titleTextView, View.ALPHA, 1f).setDuration(100),
            ObjectAnimator.ofFloat(binding?.nameTextView, View.ALPHA, 1f).setDuration(100),
            ObjectAnimator.ofFloat(binding?.nameEditTextLayout, View.ALPHA, 1f).setDuration(100),
            ObjectAnimator.ofFloat(binding?.emailTextView, View.ALPHA, 1f).setDuration(100),
            ObjectAnimator.ofFloat(binding?.emailEditTextLayout, View.ALPHA, 1f).setDuration(100),
            ObjectAnimator.ofFloat(binding?.passwordTextView, View.ALPHA, 1f).setDuration(100),
            ObjectAnimator.ofFloat(binding?.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100),
            ObjectAnimator.ofFloat(binding?.registerButton, View.ALPHA, 1f).setDuration(100)
        )

        AnimatorSet().apply {
            playSequentially(animators)
            startDelay = 50
        }.start()
    }
}

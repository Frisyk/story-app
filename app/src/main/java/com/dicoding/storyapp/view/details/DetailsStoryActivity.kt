package com.dicoding.storyapp.view.details

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.storyapp.data.model.DetailsStory
import com.dicoding.storyapp.databinding.ActivityDetailsStoryBinding

class DetailsStoryActivity : AppCompatActivity() {
    private var _binding: ActivityDetailsStoryBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailsStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        bindDetails()
    }

    private fun bindDetails() {
        val detailsStoryData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_STORY, DetailsStory::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_STORY)
        }

        binding?.tvname?.text = detailsStoryData?.name?.uppercase()
        binding?.tvdetail?.text = detailsStoryData?.description
        binding?.let {
            binding?.ivdetail?.let { it1 ->
                Glide.with(it.root)
                    .load(detailsStoryData?.photoUrl)
                    .into(it1)
            }
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}
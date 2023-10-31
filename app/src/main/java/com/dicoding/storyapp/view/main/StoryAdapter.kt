package com.dicoding.storyapp.view.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.model.DetailsStory
import com.dicoding.storyapp.data.model.ListStoryItem
import com.dicoding.storyapp.view.details.DetailsStoryActivity

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imgItemPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        private var tvItemName: TextView = itemView.findViewById(R.id.tv_item_name)
        private var tvItemDescription: TextView = itemView.findViewById(R.id.tv_item_description)

        fun bind(story: ListStoryItem) {
            tvItemName.text = story.name.uppercase()
            tvItemDescription.text = story.description
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(imgItemPhoto)

            itemView.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(imgItemPhoto, "image"),
                        Pair(tvItemName, "name"),
                        Pair(tvItemDescription, "description"),
                    )

                val detailsStory = DetailsStory(
                    story.name,
                    story.description,
                    story.photoUrl
                )

                val intentDetail = Intent(itemView.context, DetailsStoryActivity::class.java)
                intentDetail.putExtra(DetailsStoryActivity.EXTRA_STORY, detailsStory)
                itemView.context.startActivity(intentDetail, optionsCompat.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.story_item, parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}

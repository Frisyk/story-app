package com.dicoding.storyapp

import com.dicoding.storyapp.data.model.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "photo + $i",
                "createdAt + $i",
                "name $i",
                "desc $i",
                i.toFloat(),
                "id + $i",
                i.toFloat()
            )
            items.add(story)
        }
        return items
    }
}
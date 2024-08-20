package com.example.hopedonationapp.HomePage

import Story
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hopedonationapp.R

class HomeStoryAdapter(
    private val onItemClick: (Story) -> Unit
) : RecyclerView.Adapter<HomeStoryAdapter.StoryViewHolder>() {
    private var stories: List<Story> = listOf()
    inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val thumbnailImageView: ImageView = itemView.findViewById(R.id.thumbnailImageView)
        private val fileNameTextView: TextView = itemView.findViewById(R.id.storyTextView)

        fun bind(story: Story) {
            fileNameTextView.text = story.fileName
            story.thumbnailBitmap?.let {
                thumbnailImageView.setImageBitmap(it)
            } ?: thumbnailImageView.setImageResource(R.drawable.hope_for_it_win_it) // Set a default image if null
            itemView.setOnClickListener { onItemClick(story) }
        }
    }
    fun submitList(newStories: List<Story>) {
        stories = newStories
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int = stories.size
}

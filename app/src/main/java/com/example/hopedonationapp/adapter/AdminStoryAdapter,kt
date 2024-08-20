package com.example.hopedonationapp.StoryAdapter

import Story
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.hopedonationapp.R
import android.widget.TextView
import android.widget.CheckBox
class AdminStoryAdapter(
    private val onStoryClick: (Story) -> Unit
) : RecyclerView.Adapter<AdminStoryAdapter.ViewHolder>() {

    private var stories: List<Story> = listOf()
    private val selectedStories = mutableListOf<Story>()

    fun submitList(newStories: List<Story>) {
        stories = newStories
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val thumbnailImageView: ImageView = itemView.findViewById(R.id.thumbnailImageView)
        private val fileNameTextView: TextView = itemView.findViewById(R.id.storyTextView)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)

        fun bind(story: Story) {
            fileNameTextView.text = "Let's see the new story"
            story.thumbnailBitmap?.let {
                thumbnailImageView.setImageBitmap(it)
            } ?: thumbnailImageView.setImageResource(R.drawable.hope_for_it_win_it) // Default image

            checkBox.isChecked = selectedStories.contains(story)
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (selectedStories.size < 8) {
                        selectedStories.add(story)
                    } else {
                        checkBox.isChecked = false
                    }
                } else {
                    selectedStories.remove(story)
                }
            }

            itemView.setOnClickListener { onStoryClick(story) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_item_story, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stories[position])
    }
    override fun getItemCount(): Int = stories.size

    fun getSelectedStories(): List<Story> = selectedStories
}

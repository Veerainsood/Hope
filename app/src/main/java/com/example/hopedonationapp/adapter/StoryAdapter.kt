import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hopedonationapp.R

class StoryAdapter(private val onStoryClick: (Story) -> Unit) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {
    private var stories: List<Story> = listOf()

    fun submitList(newStories: List<Story>) {
        stories = newStories
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val thumbnailImageView: ImageView = itemView.findViewById(R.id.thumbnailImageView)
        private val fileNameTextView: TextView = itemView.findViewById(R.id.storyTextView)

        fun bind(story: Story) {
            fileNameTextView.text = "HERE IS A NEW STORY"
            story.thumbnailBitmap?.let {
                thumbnailImageView.setImageBitmap(it)
            } ?: thumbnailImageView.setImageResource(R.drawable.hope_for_it_win_it) // Set a default image if null
            itemView.setOnClickListener { onStoryClick(story) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int = stories.size
}

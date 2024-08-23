

import Story
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.hopedonationapp.R
import android.widget.TextView
import android.widget.CheckBox
import com.example.hopedonationapp.adapter.UpdateDocReq
import org.w3c.dom.Document

class AdminVerificationAdapter(
    private val onClick: (UpdateDocReq) -> Unit
) : RecyclerView.Adapter<AdminVerificationAdapter.ViewHolder>() {

    private var Documents: List<UpdateDocReq> = listOf()
    private val selectedDocuments = mutableListOf<UpdateDocReq>()

    fun submitList(newDocs: List<UpdateDocReq>) {
        Documents = newDocs
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fileNameTextView: TextView = itemView.findViewById(R.id.storyTextView)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)

        fun bind(Doc: UpdateDocReq) {
            fileNameTextView.text = "New Document"

            checkBox.isChecked = selectedDocuments.contains(Doc)
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                        selectedDocuments.add(Doc)
                } else {
                    selectedDocuments.remove(Doc)
                }
            }

            itemView.setOnClickListener { onClick(Doc) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.verification_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(Documents[position])
    }
    override fun getItemCount(): Int = Documents.size

    fun getSelectedDocuments(): MutableList<UpdateDocReq> = selectedDocuments
}

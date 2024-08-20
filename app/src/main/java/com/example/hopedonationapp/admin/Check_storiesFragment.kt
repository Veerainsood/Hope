package com.example.hopedonationapp.admin

import Story
import StoryAdapter
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hopedonationapp.ARG_PARAM1
import com.example.hopedonationapp.ARG_PARAM2
import com.example.hopedonationapp.R
import com.example.hopedonationapp.SharedViewModel
import com.example.hopedonationapp.StoryAdapter.AdminStoryAdapter
import com.example.hopedonationapp.databinding.FragmentCheckStoriesBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

/**
 * A simple [Fragment] subclass.
 * Use the [check_storiesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class check_storiesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentCheckStoriesBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var selectButton: Button
    private lateinit var uploadButton: Button
    private val storage = FirebaseStorage.getInstance()
    private val selectedStories = mutableListOf<Story>()
    private lateinit var storyAdapter: AdminStoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_check_stories, container, false)

        // Now that view is non-null, initialize the components
        loadAllUserStories()
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        storyAdapter = AdminStoryAdapter { story ->
            openFile(story.fileUrl) // Opens the file when TextView is clicked
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.storiesRecyclerView)
        recyclerView.adapter = storyAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        uploadButton=view.findViewById(R.id.button3)
        view.findViewById<Button>(R.id.button3).setOnClickListener {
            val selectedStories = storyAdapter.getSelectedStories()
            if (selectedStories.isNotEmpty()) {
                uploadSelectedStoriesToHomeScreen(selectedStories)
            } else {
                Toast.makeText(context, "No stories selected!", Toast.LENGTH_SHORT).show()
            }
        }

        // Return the view after initializing everything
        return view
    }
    private fun uploadSelectedStoriesToHomeScreen(selectedStories: List<Story>) {
        sharedViewModel.setSelectedStories(selectedStories)
        Toast.makeText(context, "Selected stories uploaded!", Toast.LENGTH_SHORT).show()

        // Optionally navigate to HomeScreen
       // findNavController().navigate(R.id.action_check_storiesFragment_to_homeScreen)
    }
    private fun openFile(fileUrl: String) {
        val uri = Uri.parse(fileUrl)
        val mimeType = getMimeType(uri)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, mimeType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            // Ensure that the intent can handle a variety of applications
        }

        val chooser = Intent.createChooser(intent, "Open with")
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(chooser)
        } else {
            Toast.makeText(context, "No app found to open this file.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun loadAllUserStories() {
        val userStoriesRef = storage.reference.child("stories/")
        userStoriesRef.listAll().addOnSuccessListener { result ->
            val stories = mutableListOf<Story>()
            for (folder in result.prefixes) {
                folder.listAll().addOnSuccessListener { folderResult ->
                    for (item in folderResult.items) {
                        item.downloadUrl.addOnSuccessListener { uri ->
                            val fileName = item.name
                            val mimeType = getMimeType(uri)
                            val thumbnailBitmap = when {
                                mimeType.startsWith("image/") -> generateImageThumbnail(uri)
                                mimeType.startsWith("video/") -> generateVideoThumbnail(uri)
                                mimeType == "application/pdf" -> generatePdfThumbnail(uri)
                                else -> null
                            }

                            stories.add(Story(fileName = fileName, fileUrl = uri.toString(), thumbnailBitmap = thumbnailBitmap))
                            if (stories.size == folderResult.items.size) {
                                storyAdapter.submitList(stories)
                            }
                        }
                    }
                }
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(context, "Failed to load stories: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getMimeType(uri: Uri): String {
        val extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
        return when (extension.toLowerCase()) {
            "pdf" -> "application/pdf"
            "jpg", "jpeg", "png" -> "image/*"
            "mp4" -> "video/mp4"
            "html", "htm" -> "text/html"
            "txt" -> "text/plain"
            "doc", "docx" -> "application/msword"
            "xls", "xlsx" -> "application/vnd.ms-excel"
            "ppt", "pptx" -> "application/vnd.ms-powerpoint"
            // Add more cases as needed
            else -> "*/*" // Fallback to generic MIME type
        }
    }

    private fun generateImageThumbnail(uri: Uri): Bitmap? {
        return try {
            val inputStream = context?.contentResolver?.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }
    private fun generateVideoThumbnail(uri: Uri): Bitmap? {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, uri)
            retriever.getFrameAtTime(0)
        } catch (e: Exception) {
            null
        }
    }
    private fun generatePdfThumbnail(uri: Uri): Bitmap? {
        return try {
            // Open the ParcelFileDescriptor from the Uri
            val parcelFileDescriptor = context?.contentResolver?.openFileDescriptor(uri, "r")
            parcelFileDescriptor?.let {
                // Get the FileDescriptor from the ParcelFileDescriptor
                val fileDescriptor = it.fileDescriptor

                // Create a PdfRenderer instance
                val pdfRenderer = PdfRenderer(parcelFileDescriptor)
                val pageCount = pdfRenderer.pageCount

                // Check if the PDF has at least one page
                if (pageCount > 0) {
                    // Render the first page as a Bitmap
                    val page = pdfRenderer.openPage(0)
                    val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                    // Clean up resources
                    page.close()
                    pdfRenderer.close()

                    bitmap
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment check_storiesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            check_storiesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

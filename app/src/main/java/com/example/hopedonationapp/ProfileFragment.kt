package com.example.hopedonationapp

import Story
import com.example.hopedonationapp.adapter.StoryAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.example.hopedonationapp.models.story
import com.google.firebase.storage.FirebaseStorage
import androidx.activity.result.contract.ActivityResultContracts
//import com.example.hopedonationapp.com.example.hopedonationapp.adapter.StoryAdapter.com.example.hopedonationapp.adapter.StoryAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import com.bumptech.glide.Glide

class ProfileFragment : Fragment() {
    private lateinit var fileUri: Uri
    private lateinit var auth: FirebaseAuth
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var storageRef: StorageReference
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            fileUri = it
            val fileName = getFileName(uri)
            uploadStory(fileUri, fileName)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance().reference

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val uploadButton: Button = view.findViewById(R.id.uploadButton)
        val loadStoriesButton: Button = view.findViewById(R.id.loadStoriesButton)

        storyAdapter = StoryAdapter { story ->
            openFile(story.fileUrl) // Opens the file when TextView is clicked
        }
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.adapter = storyAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        uploadButton.setOnClickListener {
            openFilePicker()
        }

        loadStoriesButton.setOnClickListener {
            loadUserStories()
        }

        return view
    }

    private fun openFilePicker() {
        getContent.launch("*/*")
    }

    private fun loadUserStories() {
        val userStoriesRef = storageRef.child("stories/${auth.currentUser?.uid}/")

        userStoriesRef.listAll()
            .addOnSuccessListener { listResult ->
                val stories = mutableListOf<Story>()
                for (item in listResult.items) {
                    item.downloadUrl.addOnSuccessListener { uri ->
                        val fileName = item.name
                        val mimeType = getMimeType(uri)
                        val thumbnailBitmap = when {
                            mimeType.startsWith("image/") -> generateImageThumbnail(uri)
                            mimeType.startsWith("video/") -> generateVideoThumbnail(uri)
                            mimeType == "application/pdf" -> generatePdfThumbnail(uri)
                            else -> null
                        }

                        // Add the story with the thumbnail to the list
                        stories.add(Story(fileName = fileName, fileUrl = uri.toString(), thumbnailBitmap = thumbnailBitmap))
                        if (stories.size == listResult.items.size) {
                            displayStories(stories)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to load stories: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun displayStories(stories: List<Story>) {
        storyAdapter.submitList(stories)
    }


    private fun generateImageThumbnail(uri: Uri): Bitmap? {
        return try {
            val futureTarget = Glide.with(this)
                .asBitmap()
                .load(uri)
                .submit()
            futureTarget.get()
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


    // Helper function to get the MIME type from the file URL
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


    private fun uploadStory(uri: Uri, fileName: String) {
        val fileRef = storageRef.child("stories/${auth.currentUser?.uid}/${System.currentTimeMillis()}_$fileName")

        fileRef.putFile(uri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { url ->
                    Log.d("UploadSuccess", "File URL: $url")
                    loadUserStories()  // Refresh the list after uploading
                }.addOnFailureListener { e ->
                    Log.e("UploadFailure", "Error getting download URL", e)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("UploadFailure", "Upload failed", e)
            }
    }

    private fun getFileName(uri: Uri): String {
        var name = "Unknown"
        val cursor = context?.contentResolver?.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                name = it.getString(nameIndex)
            }
        }
        return name
    }
}

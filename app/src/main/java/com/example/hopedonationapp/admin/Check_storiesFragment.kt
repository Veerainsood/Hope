package com.example.hopedonationapp.admin

import Story
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hopedonationapp.R
import com.example.hopedonationapp.StoryAdapter.AdminStoryAdapter
import com.example.hopedonationapp.databinding.FragmentCheckStoriesBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import okhttp3.OkHttpClient
import okhttp3.Request


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
    private lateinit var uploadButton: Button
    private val storage = FirebaseStorage.getInstance()
    private val selectedStories = mutableListOf<Story>()
    private lateinit var storyAdapter: AdminStoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_check_stories, container, false)

        // Now that view is non-null, initialize the components
        loadAllUserStories()
        storyAdapter = AdminStoryAdapter { story ->
            openFile(story.fileUrl) // Opens the file when TextView is clicked
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.storiesRecyclerView)
        recyclerView.adapter = storyAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        uploadButton = view.findViewById(R.id.button3)
        uploadButton.setOnClickListener {
            val selectedStories = storyAdapter.getSelectedStories()
            if (selectedStories.isNotEmpty()) {
                uploadSelectedStoriesToFirebase(selectedStories)
            } else {
                Toast.makeText(context, "No stories selected!", Toast.LENGTH_SHORT).show()
            }
        }


        // Return the view after initializing everything
        return view
    }

    private fun uploadSelectedStoriesToFirebase(stories: List<Story>) {
        val selectedStoriesRef = storage.reference.child("stories/selected_stories/")

        for (story in stories) {
            val fileUrl = story.fileUrl

            // Check if the file URL is valid
            if (fileUrl.startsWith("https://")) {
                // Download the file using the URL
                val fileName = Uri.parse(fileUrl).lastPathSegment ?: "unknown"
                val fileRef = selectedStoriesRef.child(fileName)

                // Create a temporary file to download the content
                val localFile = File.createTempFile("temp", null, context?.cacheDir)

                // Download the file from the URL
                Firebase.storage.getReferenceFromUrl(fileUrl)
                    .getFile(localFile)
                    .addOnSuccessListener {
                        // Upload the downloaded file to Firebase Storage
                        val fileUri = Uri.fromFile(localFile)
                        fileRef.putFile(fileUri)
                            .addOnSuccessListener {
                                // Display success message
                                Toast.makeText(
                                    context,
                                    "${story.fileName} uploaded successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener { exception ->
                                // Display detailed error message
                                Toast.makeText(
                                    context,
                                    "Failed to upload ${story.fileName}: ${exception.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e("UploadError", "Failed to upload file ${story.fileName}", exception)
                            }
                    }
                    .addOnFailureListener { exception ->
                        // Handle the error in downloading the file
                        Toast.makeText(
                            context,
                            "Failed to download ${story.fileName}: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("DownloadError", "Failed to download file ${story.fileName}", exception)
                    }
            } else {
                // Handle the case where the fileUrl is a local file URI or invalid
                val fileUri = Uri.parse(fileUrl)
                val fileName = fileUri.lastPathSegment ?: "unknown"
                val fileRef = selectedStoriesRef.child(fileName)

                fileUri.let { uri ->
                    fileRef.putFile(uri)
                        .addOnSuccessListener {
                            // Display success message
                            Toast.makeText(
                                context,
                                "${story.fileName} uploaded successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { exception ->
                            // Display detailed error message
                            Toast.makeText(
                                context,
                                "Failed to upload ${story.fileName}: ${exception.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("UploadError", "Failed to upload file ${story.fileName}", exception)
                        }
                }
            }
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
                                //mimeType == "application/pdf" -> generatePdfThumbnail(uri)
                                else -> null
                            }

                            stories.add(
                                Story(
                                    fileName = fileName,
                                    fileUrl = uri.toString(),
                                    thumbnailBitmap = thumbnailBitmap
                                )
                            )
                            if (stories.size == folderResult.items.size) {
                                storyAdapter.submitList(stories)
                            }
                        }
                    }
                }
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(
                context,
                "Failed to load stories: ${exception.message}",
                Toast.LENGTH_SHORT
            ).show()
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
}


//    private fun generatePdfThumbnail(uri: Uri): Bitmap? {
//        return try {
//            // Step 1: Download the PDF file from the remote URL
//            val pdfFile = downloadPdfFile(uri.toString()) ?: return null
//
//            // Step 2: Open the PDF file using PdfRenderer
//            val parcelFileDescriptor = context?.contentResolver?.openFileDescriptor(Uri.fromFile(pdfFile), "r")
//            parcelFileDescriptor?.let {
//                val pdfRenderer = PdfRenderer(parcelFileDescriptor)
//                val pageCount = pdfRenderer.pageCount
//
//                // Step 3: Render the first page as a Bitmap
//                if (pageCount > 0) {
//                    val page = pdfRenderer.openPage(0)
//                    val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
//                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
//
//                    // Clean up resources
//                    page.close()
//                    pdfRenderer.close()
//
//                    bitmap
//                } else {
//                    null
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    // Helper function to download the PDF file from the URL
//    private fun downloadPdfFile(url: String): File? {
//        return try {
//            val client = OkHttpClient()
//            val request = Request.Builder().url(url).build()
//            val response = client.newCall(request).execute()
//
//            // Safely access the response body
//            val inputStream: InputStream? = response.body?.byteStream()
//            if (inputStream != null) {
//                val file = File(context?.cacheDir, "downloaded_pdf.pdf")
//                val outputStream = FileOutputStream(file)
//
//                inputStream.copyTo(outputStream)
//                outputStream.close()
//                inputStream.close()
//
//                file
//            } else {
//                null
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }




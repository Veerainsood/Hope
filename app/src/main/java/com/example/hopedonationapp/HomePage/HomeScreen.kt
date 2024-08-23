package com.example.hopedonationapp.HomePage

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
import android.widget.MediaController
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.hopedonationapp.R
import com.example.hopedonationapp.StoryAdapter.AdminStoryAdapter
import com.example.hopedonationapp.databinding.FragmentHomeScreenBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class HomeScreen : Fragment() {
    private lateinit var binding: FragmentHomeScreenBinding
    private lateinit var homeAdapter: HomeStoryAdapter
    private lateinit var storageRef: StorageReference
    private val storage = FirebaseStorage.getInstance()
    private lateinit var storyAdapter: HomeStoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)

        storyAdapter = HomeStoryAdapter { story ->
            openFile(story)
        }

        // Set up the RecyclerView
        val recyclerView = binding.topStoriesRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = storyAdapter
        loadSelectedStoriesFromFirebase()
        binding.clothes.setOnClickListener { openGoogleMaps("anath+ashram+near+me") }
        binding.food.setOnClickListener { openGoogleMaps("old+age+home+near+me") }
        binding.Books.setOnClickListener { openGoogleMaps("public+library+near+me") }
        binding.Meds.setOnClickListener { openGoogleMaps("public+hospital+near+me") }
        binding.blood.setOnClickListener { openGoogleMaps("blood+bank+near+me") }
        binding.organ.setOnClickListener { openGoogleMaps("public+hospital+near+me") }
        binding.Gaushala.setOnClickListener { openGoogleMaps("goshala+near+me") }
        binding.jkp.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://jkyog.in/en/donate/"))) }
        binding.redHeart.setOnClickListener { findNavController().navigate(R.id.action_homeScreen_to_profileFragment) }
        binding.greenHeart.setOnClickListener { findNavController().navigate(R.id.action_homeScreen_to_verifiedCharityOrg) }




        return binding.root
    }

    private fun openFile(story: Story) {
        val fileUri = Uri.parse(story.fileUrl)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(fileUri, getMimeType(fileUri))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(context, "No app found to open this file.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadSelectedStoriesFromFirebase() {
        val selectedStoriesRef = storage.reference.child("selected_stories/")
        selectedStoriesRef.listAll().addOnSuccessListener { result ->
            val stories = mutableListOf<Story>()
            for (item in result.items) {
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
                    storyAdapter.submitList(stories)
                }
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(context, "Failed to load stories: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }
//    override fun onPause() {
//        super.onPause()
//        videoView.pause()
//    }

//    override fun onResume() {
//        super.onResume()
//        videoView.start()
//    }
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
    private fun openGoogleMaps(search: String) {
        val getUrl = Uri.parse("https://www.google.com/maps/search/$search")
        val mapOpeningIntent = Intent(Intent.ACTION_VIEW, getUrl).apply {
            setPackage("com.google.android.apps.maps")
        }
        if (mapOpeningIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(mapOpeningIntent)
        } else {
            startActivity(Intent(Intent.ACTION_VIEW, getUrl))
        }
    }
}

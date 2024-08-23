package com.example.hopedonationapp.admin

import AdminVerificationAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hopedonationapp.R
import com.example.hopedonationapp.StoryAdapter.AdminStoryAdapter
import com.example.hopedonationapp.adapter.UpdateDocReq
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class Verify_CompanyFragment : Fragment() {

    private val storage = FirebaseStorage.getInstance()
    private lateinit var Adapter: AdminVerificationAdapter
    private lateinit var uploadButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_verify__company, container, false)

        loadAllDocuments()
        Adapter = AdminVerificationAdapter{ Document ->
            openFile(Document.textView) // Opens the file when TextView is clicked
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.companyRecyclerView)
        recyclerView.adapter = Adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        uploadButton = view.findViewById(R.id.uploadButton)
        uploadButton.setOnClickListener {
            val selectedDocuments = Adapter.getSelectedDocuments()
            if (selectedDocuments.isNotEmpty()) {
                uploadSelectedDocumentsToFirebase(selectedDocuments)
            } else {
                Toast.makeText(context, "No Document selected!", Toast.LENGTH_SHORT).show()
            }
        }


        // Return the view after initializing everything
        return view
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
    private fun loadAllDocuments() {
        // Define the reference to the folder in Firebase Storage where documents are stored
        val documentsRef = storage.reference.child("uploads/")

        // Retrieve the list of all documents in the folder
        documentsRef.listAll().addOnSuccessListener { result ->
            val documents = mutableListOf<UpdateDocReq>()

            // Iterate over each item in the result and add it to the list of documents
            for(folder in result.prefixes) {
                folder.listAll().addOnSuccessListener { folderResult ->
                    for (item in result.items) {
                        item.downloadUrl.addOnSuccessListener { uri ->
                            val fileName = item.name // Get the file name
                            val document = UpdateDocReq(
                                textView = uri.toString(),  // File URL
                                buttonText = fileName      // Display the file name as button text
                            )
                            documents.add(document)

                            // Update the adapter once all documents are loaded
                            Adapter.submitList(documents)
                        }
                    }
                }
            }
        }.addOnFailureListener { exception ->
            Log.e("Verify_CompanyFragment", "Failed to list documents", exception)
            Toast.makeText(context, "Failed to load documents: ${exception.message}", Toast.LENGTH_SHORT).show()
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

    private fun uploadSelectedDocumentsToFirebase(uploads: List<UpdateDocReq>) {
        val selectedStoriesRef = storage.reference.child("uploads/verified/")

        // Iterate over each document request
        for (upload in uploads) {
            // Assuming textView contains the file path or Uri to upload
            val fileUri = Uri.parse(upload.textView) // Adjust this line based on the actual usage
            val fileName = fileUri.lastPathSegment ?: "default_name"

            // Create a reference to the file location in Firebase Storage
            val fileRef = selectedStoriesRef.child(fileName)

            // Start the upload task
            fileRef.putFile(fileUri)
                .addOnSuccessListener { taskSnapshot ->
                    // Handle successful uploads here
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                        Log.d("Upload", "File uploaded successfully. Download URL: $downloadUri")
                        Toast.makeText(context, "${upload.buttonText} uploaded successfully.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle unsuccessful uploads here
                    Log.e("Upload", "File upload failed: ${exception.message}")
                    Toast.makeText(context, "Failed to upload ${upload.buttonText}: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            fileRef.delete()
        }
    }


}
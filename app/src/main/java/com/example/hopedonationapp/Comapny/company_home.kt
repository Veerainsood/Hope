package com.example.hopedonationapp.Comapny

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hopedonationapp.R
import com.example.hopedonationapp.adapter.RecyclerViewAdapter
import com.example.hopedonationapp.adapter.UpdateDocReq
import com.example.hopedonationapp.databinding.FragmentCompanyHomeBinding
import com.example.hopedonationapp.utils.Utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage


class company_home : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentCompanyHomeBinding
    private lateinit var textViewArray: Array<String>
    private lateinit var buttonTextArray: Array<String>
    private lateinit var newArrayList: ArrayList<UpdateDocReq>
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private val PDF = 0
    private var selectedDocPosition: Int = -1
    private var storageFirebase = FirebaseStorage.getInstance().reference
    private lateinit var buttonStates: Array<Boolean>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCompanyHomeBinding.inflate(layoutInflater)

        textViewArray = arrayOf<String>(
            "Section 12A"," incorporation document", "registration certificate","(FCRA), if applicable"
            , "(23C)section 10", "organisation’s operations"
        )
        buttonTextArray = arrayOf<String>(
            "Upload 12A","Upload incorp doc","Upload reg cert","Upload FCRA","Upload 23C","Upload operations"
        )
        buttonStates = Array(textViewArray.size) { false }
        binding.progressBar2.visibility = View.GONE
        newArrayList = ArrayList()
        binding.FinalVerification.isEnabled = false
        for (i in textViewArray.indices) {
            newArrayList.add(UpdateDocReq(textViewArray[i], buttonTextArray[i]))
        }

        binding.Recview.layoutManager = LinearLayoutManager(context)
        binding.Recview.setHasFixedSize(true)
        binding.Recview.adapter = RecyclerViewAdapter(newArrayList) { position ->
            selectedDocPosition = position
            startPDFSelection()
        }
        if(binding.FinalVerification.isEnabled)
        {
            binding.FinalVerification.setOnClickListener {
                Utils.showToast(requireContext(),"We have succesfully collected your information\nOur Admins will verify your application shortly")
            }
        }
        return binding.root
    }


    private fun startPDFSelection() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/pdf"
            addCategory(Intent.CATEGORY_OPENABLE)
        }

        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PDF && resultCode == Activity.RESULT_OK && data != null) {
            val uri: Uri = data.data!!
            val userName = arguments?.getString("AccountName")
            val fileReference = storageFirebase.child("uploads/$userName/{$selectedDocPosition}.pdf")
            binding.progressBar2.visibility = View.VISIBLE

            val uploadTask = fileReference.putFile(uri)
            uploadTask.addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    binding.progressBar2.visibility = View.GONE
                    val downloadUrl = uri.toString()
                    val button = binding.Recview.findViewHolderForAdapterPosition(selectedDocPosition)?.itemView?.findViewById<Button>(
                        R.id.button3)

                    button?.apply {
                        isEnabled = false
                        setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey))
                    }
                    buttonStates[selectedDocPosition] = true
                    binding.FinalVerification.isEnabled = buttonStates.all{ it }

                    Utils.showToast(requireContext(),"Success!!")
                }.addOnFailureListener { exception ->
                    binding.progressBar2.visibility = View.GONE
                    Utils.showToast(requireContext(),"Failed")
                }
            }.addOnFailureListener { exception ->
                binding.progressBar2.visibility = View.GONE
                Utils.showToast(requireContext(),"Failed")
            }
        }

    }


}
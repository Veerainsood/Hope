package com.example.hopedonationapp.Comapny

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.example.hopedonationapp.R
import com.example.hopedonationapp.databinding.FragmentCompanyRegistrationBinding
import com.example.hopedonationapp.utils.Utils
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class CompanyRegistration : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentCompanyRegistrationBinding
    private  lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompanyRegistrationBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()


        binding.RegisterButton.setOnClickListener {
            val email = binding.registeremail.text.toString()
            val confirmpass = binding.confirmpassword.text.toString()
            val pass = binding.registerpassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                if (pass == confirmpass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            findNavController().navigate(R.id.action_company_registration2_to_companyLogin3)
                        }
                    }
                } else {
                    Utils.showToast(requireContext(), "Password does not match")
                }
            }
            else
            {
                Toast.makeText(requireActivity(), "Please enter text in both email and password", Toast.LENGTH_SHORT).show()
            }
        }
        binding.AlreadyReg.setOnClickListener {
            findNavController().navigate(R.id.action_company_registration2_to_companyLogin3)
            try {

            }
            catch (e: Exception){
                Toast.makeText(requireContext(), "Navigation Failed", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

}
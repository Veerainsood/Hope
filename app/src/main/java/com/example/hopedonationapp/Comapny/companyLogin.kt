package com.example.hopedonationapp.Comapny

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.example.hopedonationapp.R
import com.example.hopedonationapp.databinding.FragmentCompanyLoginBinding
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 * Use the [CompanyLogin.newInstance] factory method to
 * create an instance of this fragment.
 */
class companyLogin : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentCompanyLoginBinding
    private  lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        firebaseAuth = FirebaseAuth.getInstance()

        binding = FragmentCompanyLoginBinding.inflate(layoutInflater)

        binding.LoginButton.setOnClickListener {
            val email = binding.registeremail.text.toString()
            val pass = binding.registerpassword.text.toString()
            val bundle = Bundle()
            bundle.putString("AccountName", email.substringBefore('@'))
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        findNavController().navigate(R.id.action_companyLogin3_to_company_home,bundle)
                    } else {
                        Toast.makeText(requireActivity(), "Wrong Password !!!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else
            {
                Toast.makeText(
                    requireActivity(),
                    "Please enter text in both email and password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }


}
package com.example.hopedonationapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hopedonationapp.R
import com.example.hopedonationapp.databinding.FragmentAdminEntranceBinding
import com.example.hopedonationapp.utils.Utils
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 * Use the [admin_entrance.newInstance] factory method to
 * create an instance of this fragment.
 */
class admin_entrance : Fragment() {
    // TODO: Rename and change types of parameters
    private  lateinit var binding : FragmentAdminEntranceBinding
    private  lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminEntranceBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.editTextTextEmailAddress.setOnClickListener {
            val intent = Intent(requireActivity(), admin_entrance::class.java)
            startActivity(intent)
        }
        binding.SignInButton.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            val pass = binding.editTextTextPassword.text.toString()

                if (email.isNotEmpty() && pass.isNotEmpty()) {

                    if(email == "hopeadmin@gmail.com") {
                    firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            findNavController().navigate(R.id.action_admin_entrance_to_adminHomeFragment)
                        } else {
                            Toast.makeText(requireActivity(), "Wrong Password !!!", Toast.LENGTH_SHORT).show()
                        }
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
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_admin_entrance, container, false)
    }

}
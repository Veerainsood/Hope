package com.example.hopedonationapp.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController

import com.example.hopedonationapp.R
import com.example.hopedonationapp.databinding.FragmentAdminHomeBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AdminHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminHomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private  lateinit var binding : FragmentAdminHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminHomeBinding.inflate(layoutInflater)
        binding.button6.setOnClickListener(){
            findNavController().navigate(R.id.action_adminHomeFragment_to_check_storiesFragment)
        }

        return binding.root;
    }

}

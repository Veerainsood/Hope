package com.example.hopedonationapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hopedonationapp.databinding.FragmentHomeScreenBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
internal const val ARG_PARAM1 = "param1"
internal const val ARG_PARAM2 = "param2"
class HomeScreen : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private  lateinit var binding : FragmentHomeScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeScreenBinding.inflate(layoutInflater)
//        return inflater.inflate(R.layout.fragment_splsh, container, false)

        return binding.root
    }

}

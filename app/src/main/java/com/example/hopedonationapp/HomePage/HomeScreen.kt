package com.example.hopedonationapp.HomePage

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.fragment.app.Fragment

import com.example.hopedonationapp.R
import com.example.hopedonationapp.databinding.FragmentHomeScreenBinding
import com.example.hopedonationapp.databinding.FragmentSplshBinding

/**
 * A simple [Fragment] subclass.
 * Use the [HomeScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeScreen : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentHomeScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeScreenBinding.inflate(layoutInflater)
        val videoView : VideoView = binding.videoView2
        val packageName = "android.resource://" + requireContext().packageName + "/" + R.raw.charity
        val uri = Uri.parse(packageName)
        videoView.setVideoURI(uri)

        val mediaController = MediaController(requireContext())
        videoView.setMediaController(mediaController)
        mediaController.setAnchorView(videoView)
        videoView.start()
        return binding.root

    }


}
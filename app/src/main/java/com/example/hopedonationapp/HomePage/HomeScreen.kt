package com.example.hopedonationapp.HomePage

import android.content.Intent
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
    private lateinit var videoView: VideoView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeScreenBinding.inflate(layoutInflater)
        videoView = binding.videoView2
        val packageName = "android.resource://" + requireContext().packageName + "/" + R.raw.charity
        val uri = Uri.parse(packageName)
        videoView.setVideoURI(uri)
        videoView.setOnTouchListener { _, _ -> true }

        val mediaController = MediaController(requireContext())
        videoView.setMediaController(mediaController)
        mediaController.setAnchorView(videoView)
        videoView.start()
        videoView.setOnCompletionListener {
            videoView.start() //for infinite loop
        }

        val clothes = binding.clothes.setOnClickListener {
            openGoogleMaps("anath+ashram+near+me")
        }
        val food = binding.food.setOnClickListener {
            openGoogleMaps("old+age+home+near+me")
        }
        val books = binding.Books.setOnClickListener {
            openGoogleMaps("public+library+near+me")
        }
        val meds = binding.Meds.setOnClickListener {
            openGoogleMaps("public+hospital+near+me")
        }

        return binding.root

    }
    //the video was stopping whenever external links were clicked... so these functions handle
    //the pausing and resuming the video when this fragment is out of focus.
    override fun onPause() {
        super.onPause()
        videoView.pause()
    }

    override fun onResume() {
        super.onResume()
        videoView.start()
    }
    private fun openGoogleMaps(search : String)
    {
        val getUrl = Uri.parse("https://www.google.com/maps/search/$search")
        val mapOpeningIntention = Intent(Intent.ACTION_VIEW, getUrl)
        mapOpeningIntention.setPackage("com.google.android.apps.maps")
        if (mapOpeningIntention.resolveActivity(requireContext().packageManager) != null) {
            startActivity(mapOpeningIntention)
            //if app is there it will open in app otherwise
        } else {
            //it will open in browser
            startActivity(Intent(Intent.ACTION_VIEW, getUrl))
        }

    }

}
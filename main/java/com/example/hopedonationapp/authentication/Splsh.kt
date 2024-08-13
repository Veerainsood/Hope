package com.example.hopedonationapp.authentication
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hopedonationapp.R
import com.example.hopedonationapp.databinding.FragmentSplshBinding


class Splsh : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private  lateinit var binding :FragmentSplshBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplshBinding.inflate(layoutInflater)
//        return inflater.inflate(R.layout.fragment_splsh, container, false)
//        Handler(Looper.getMainLooper()).postDelayed({
//        findNavController().navigate(R.id.action_splsh_to_signInFragment)
//        },1000)
        on_admin_button_click()
        on_user_button_click()
        return binding.root
    }
    private fun on_admin_button_click(){
        binding.button3.setOnClickListener {
            findNavController().navigate(R.id.action_splsh_to_admin_entrance)
        }
    }
    private fun on_user_button_click() {
        binding.button4.setOnClickListener {
            findNavController().navigate(R.id.action_splsh_to_signInFragment)
        }
    }

}
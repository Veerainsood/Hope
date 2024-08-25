package com.example.hopedonationapp.authentication
import android.os.Bundle
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

        binding = FragmentSplshBinding.inflate(layoutInflater)
        onCompanyButtonClick()
        onAdminButtonClick()
        onUserButtonClick()
        return binding.root
    }
    private fun onCompanyButtonClick()
    {
        binding.company.setOnClickListener {
            findNavController().navigate(R.id.action_splsh_to_company_registration2)
        }
    }
    private fun onAdminButtonClick(){
        binding.admin.setOnClickListener {
            findNavController().navigate(R.id.action_splsh_to_admin_entrance)
        }
    }
    private fun onUserButtonClick() {
        binding.user.setOnClickListener {
            findNavController().navigate(R.id.action_splsh_to_signInFragment)
        }
    }

}
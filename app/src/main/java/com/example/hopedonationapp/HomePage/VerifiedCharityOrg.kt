package com.example.hopedonationapp.HomePage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.example.hopedonationapp.R
import com.example.hopedonationapp.databinding.VerifiedCharityOrgBinding
import com.example.hopedonationapp.utils.Utils


class VerifiedCharityOrg : Fragment() {
    private lateinit var binding : VerifiedCharityOrgBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = VerifiedCharityOrgBinding.inflate(layoutInflater)

        press(binding.JKYog,"JK-Yog")
        press(binding.everest,"Everest")
        press(binding.CryOrg,"Cry Org")
        press(binding.hlepAgeindia,"Help Age India")
        press(binding.Smile,"Smile Org")
        press(binding.giveOrg,"Give Org")

        return binding.root
    }

    private fun press(org : ImageButton, orgName: String)
    {
        org.setOnClickListener{
            try{
                val bundle= Bundle().apply {
                    putString("Organization",orgName)
                }
                findNavController().navigate(R.id.action_verifiedCharityOrg_to_payments,bundle)
            }
            catch (e: Exception)
            {
                Utils.showToast(requireContext(), e.toString())
            }
        }
    }
}
package com.example.hopedonationapp.authentication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hopedonationapp.R
import com.example.hopedonationapp.utils.Utils
//import com.example.hopedonationapp.activity.UsersMainActivity

import com.example.hopedonationapp.databinding.FragmentOtpBinding
import com.example.hopedonationapp.models.Users
import kotlinx.coroutines.launch


class OTPFragment : Fragment() {

    private lateinit var binding: FragmentOtpBinding
    private lateinit var userNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtpBinding.inflate(layoutInflater)

        getUserNumber()
        sendOTP()
//        customizingEnteringOTP()
        onVerifyButtonClick()

        return binding.root
    }

    private fun onVerifyButtonClick() {
        binding.button2.setOnClickListener {
            Utils.showDialog(requireContext(), "One Second")

            val otp = binding.PhoneNumDisp.text.toString()
            if (otp.length <6) {
                Utils.showToast(requireContext(), "please enter 6 len otp")
            } else {
                verifyOtp(otp)
            }

        }
    }

    private fun verifyOtp(otp: String) {
        Utils.hideDialog()
        Utils.showToast(requireContext(), "Login Succesful")
        findNavController().navigate(R.id.action_otpFragment_to_homeScreen)
    }

    private fun sendOTP() {
        Utils.showDialog(requireContext(), "Sending OTP ...")
        Utils.hideDialog()
        Utils.showToast(requireContext(), "Otp sent...")
    }


    private fun getUserNumber() {
        val bundle = arguments
        userNumber = bundle?.getString("number").toString()
        binding.UserPhone.text = userNumber
    }

}
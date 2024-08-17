package com.example.hopedonationapp.authentication


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hopedonationapp.R
import com.example.hopedonationapp.databinding.FragmentOtpBinding
import com.example.hopedonationapp.utils.Utils
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import java.util.concurrent.TimeUnit


class OTPFragment : Fragment() {

    private lateinit var binding: FragmentOtpBinding
    private lateinit var otp: String
    private lateinit var resendingToken: ForceResendingToken
    private lateinit var phone:String
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtpBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        phone = arguments?.getString("number")!!
        binding.textView9.text = "${this.phone}"
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        binding.button2.setOnClickListener {

            val typedotp =binding.PhoneNumDisp.text.toString()
            if (typedotp.length <6) {
                Utils.showToast(requireContext(), "please enter 6 len otp")
            } else {
                if (typedotp.isNotEmpty()) {
                    if (typedotp.length == 6) {
                        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(otp, typedotp)
                        signInWithPhoneAuthCredential(credential)
                    }
                    else
                    {
                        Toast.makeText(requireContext(), "Please Enter Correct OTP", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Please Enter OTP", Toast.LENGTH_SHORT).show()
                }

            }

        }
        return binding.root
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Toast.makeText(requireContext(), "Authenticate Successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_otpFragment_to_homeScreen)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {

            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Utils.showDialog(requireContext(), "Sending OTP ...")
            Utils.hideDialog()
            Utils.showToast(requireContext(), "Otp sent...")
            Utils.hideDialog()
            otp = verificationId
            resendingToken = token
        }
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Toast.makeText(requireContext(), "Authenticate Successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_otpFragment_to_homeScreen)
            } else {
                // Sign in failed, display a message and update the UI
                Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    Utils.showToast(requireContext(),"Invalid Otp try again")
                }
                // Update UI
            }
        }
    }

}
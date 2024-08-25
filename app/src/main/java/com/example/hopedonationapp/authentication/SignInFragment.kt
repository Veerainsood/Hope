package com.example.hopedonationapp.authentication


import android.R.attr.fragment
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hopedonationapp.R
import com.example.hopedonationapp.databinding.FragmentSignInBinding
import com.example.hopedonationapp.utils.Utils
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 * Use the [SignInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthViewModel : ViewModel() {
    val forceResendingToken = MutableLiveData<PhoneAuthProvider.ForceResendingToken>()
}
class SignInFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSignInBinding
    private lateinit var number: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(layoutInflater)
        val spinnerCountryCode: Spinner = binding.CountryCode
        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.country_codes, android.R.layout.simple_spinner_item)
        binding.Alert.visibility = View.VISIBLE
        binding.Ok.setOnClickListener {
            binding.Alert.visibility = View.GONE
            binding.Ok.visibility = View.GONE
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountryCode.adapter = adapter
        getUserNumber()
        on_sign_in_button_Click()
        return binding.root
    }

    private fun on_sign_in_button_Click() {
        binding.SignIn.setOnClickListener {
            number = binding.phoneNumber.text.toString()

            number = "+1$number"

            if (number.isEmpty() || number.length != 12)
            {
                Utils.showToast(requireContext(), "Please enter valid phone number")
            }
            else {

                val bundle = Bundle()
                bundle.putString("number", number)
                findNavController().navigate(R.id.action_signInFragment_to_otpFragment, bundle)
            }
        }
    }

    private fun getUserNumber(){
        binding.phoneNumber.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(number: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var len = number?.length
                if (len == 10)
                {
                    binding.SignIn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
                }
                else
                {
                    binding.SignIn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey))
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }



}
package com.example.hopedonationapp.authentication


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hopedonationapp.R
import com.example.hopedonationapp.databinding.FragmentSignInBinding
import com.example.hopedonationapp.utils.Utils


/**
 * A simple [Fragment] subclass.
 * Use the [SignInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignInFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(layoutInflater)
        setStatusBarColor()
        getUserNumber()
        on_sign_in_button_Click()
        return binding.root
    }

    private fun on_sign_in_button_Click() {
        binding.signInButton.setOnClickListener {
            val number = binding.phoneNumber.text.toString()
            if (number.isEmpty() || number.length != 10)
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

    private fun setStatusBarColor(){}

    private fun getUserNumber(){
        binding.phoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(number: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var len = number?.length
                if (len == 10) {
                    binding.signInButton.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.blue
                        )
                    )
                } else {
                    binding.signInButton.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.grey
                        )
                    )
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }


}
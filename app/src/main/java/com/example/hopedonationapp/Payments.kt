package com.example.hopedonationapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.hopedonationapp.databinding.FragmentPaymentsBinding
import com.example.hopedonationapp.databinding.VerifiedCharityOrgBinding
import com.example.hopedonationapp.utils.Utils
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Payments : Fragment() , PaymentResultListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentPaymentsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaymentsBinding.inflate(layoutInflater)
//        textView13
        Checkout.preload(requireContext())
        binding.Organizations.setText(arguments?.getString("Organization"))
        binding.button.setOnClickListener{
            val amount = binding.editTextNumberSigned.text.toString().trim().toInt()
            Utils.showToast(requireContext(),"$amount")

            try {
                savePayments(amount)
            } catch (e: NumberFormatException) {
                Utils.showToast(requireContext(),"Please enter a valid amount")
            }

        }
        return binding.root
    }

    private fun savePayments(amount: Int) {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_jFD2DuQN31BXfN")

        try {
            val optns = JSONObject()
            optns.put("name", "Hope Donations")
            optns.put("description","Helping the needy")
            optns.put("theme.color","#894AF2")
            optns.put("currency","INR")
            optns.put("amount",amount * 100)

            val retryObj = JSONObject()
            retryObj.put("enabled",true)
            retryObj.put("max_count",100)
            optns.put("retry",retryObj)

            checkout.open(requireActivity(),optns)
        }catch (e: Exception){
            Utils.showToast(requireContext(),"Error in making payment $amount your amount if debited will be /" +
                    "refunded in 24hrs")
            e.printStackTrace()
            findNavController().navigate(R.id.action_payments_to_verifiedCharityOrg)
        }
    }


    override fun onPaymentSuccess(p0: String?) {
        Utils.showToast(requireContext(),"Payment Succesful, Thankyou so much")
        Utils.showToast(requireContext(),"your payment id is : $p0")

    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Utils.showToast(requireContext(),"Payment Failed, Dont worry try again")
        findNavController().navigate(R.id.action_payments_to_verifiedCharityOrg)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment payments.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Payments().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}
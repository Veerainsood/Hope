package com.example.hopedonationapp.payments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hopedonationapp.R
import com.example.hopedonationapp.activity.MainActivity
import com.example.hopedonationapp.databinding.FragmentPaymentsBinding
import com.example.hopedonationapp.utils.Utils
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class Payments : Fragment(), PaymentResultListener {

    private var param1: String? = null
    private var param2: String? = null
    public lateinit var binding: FragmentPaymentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPaymentsBinding.inflate(layoutInflater)

        binding.popupOnSuccess.visibility = View.INVISIBLE
        binding.clickToClose.visibility = View.INVISIBLE
        Checkout.preload(requireContext())
        binding.Organizations.setText(arguments?.getString("Organization"))
        binding.button.setOnClickListener{
            val amount = binding.editTextNumberSigned.text.toString().trim().toInt()
            Utils.showToast(requireContext(), "$amount")

            try {
                savePayments(amount)
            } catch (e: NumberFormatException) {
                Utils.showToast(requireContext(), "Please enter a valid amount")
            }

        }
        return binding.root
    }

    private fun savePayments(amount: Int) {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_jFD2DuQN31BXfN")

        try {
            val optns = JSONObject()
            val orgName = arguments?.getString("Organization")
            optns.put("name", orgName)
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
            Utils.showToast(requireContext(), "Error in making payment $amount your amount if debited will be /" +
                    "refunded in 24hrs"
            )
            e.printStackTrace()
            findNavController().navigate(R.id.action_payments_to_verifiedCharityOrg)
        }
    }


    override fun onPaymentSuccess(p0: String?) {

    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Utils.showToast(requireContext(), "Payment Failed, Dont worry try again")
        findNavController().navigate(R.id.action_payments_to_verifiedCharityOrg)
    }


}
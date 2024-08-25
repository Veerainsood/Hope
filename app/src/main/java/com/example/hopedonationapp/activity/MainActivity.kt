package com.example.hopedonationapp.activity

import androidx.navigation.NavController
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.hopedonationapp.R
import com.example.hopedonationapp.payments.Payments
import com.example.hopedonationapp.utils.Utils
import com.razorpay.PaymentResultListener
import okhttp3.internal.Util

class MainActivity : AppCompatActivity() , PaymentResultListener{ // Activity == Screens
    private lateinit var myFragment: Payments
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // inflates the layout , creates connection between layout and Main Acitvity
        // R class is for recourse...   R.layout.activity_main -> conneects Main with xml representing layout elements
        //without this everything is 0/100
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this,"Payment Successfull your payment id: $p0 will be sent to you via sms",Toast.LENGTH_LONG).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Utils.showToast(this,"Payment Failed: $p1")
    }
}
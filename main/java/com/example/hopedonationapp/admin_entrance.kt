package com.example.hopedonationapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.hopedonationapp.R
import com.example.hopedonationapp.databinding.FragmentAdminEntranceBinding
import com.example.hopedonationapp.utils.Utils

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val PARG_PARAM1 = "param1"
private const val PARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [admin_entrance.newInstance] factory method to
 * create an instance of this fragment.
 */
class admin_entrance : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private  lateinit var binding : FragmentAdminEntranceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(PARG_PARAM1)
            param2 = it.getString(PARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminEntranceBinding.inflate(layoutInflater)
        on_confirm_button_click()
        return binding.root
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_admin_entrance, container, false)
    }

    private fun on_confirm_button_click(){
        binding.button.setOnClickListener {
            val password = binding.editTextTextPassword.text.toString()
            if(password == "0854493125") {
                findNavController().navigate(R.id.action_admin_entrance_to_adminHomeFragment)
            }
            else{
                Utils.showToast(requireContext(), "Wrong Password")
            }
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment admin_entrance.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            admin_entrance().apply {
                arguments = Bundle().apply {
                    putString(PARG_PARAM1, param1)
                    putString(PARG_PARAM2, param2)
                }
            }
    }
}
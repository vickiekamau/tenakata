package com.tenakata.tenakatapredictor.UI.Admission

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.tenakata.tenakatapredictor.R
import com.tenakata.tenakatapredictor.Support.InputValidator
import com.tenakata.tenakatapredictor.databinding.ActivityMainBinding.inflate
import com.tenakata.tenakatapredictor.databinding.FragmentAdmissionBinding


class Admission : Fragment() {
    private lateinit var admission: FragmentAdmissionBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        admission = FragmentAdmissionBinding.inflate(inflater, container, false)

        //gender array strings
        val property = resources.getStringArray(R.array.gender_category)
        val propertyStringAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, property)
        admission.genderAutoCompleteTextView.setAdapter(propertyStringAdapter)

        //bedroom Strings
        val noBedroom = resources.getStringArray(R.array.marital_status)
        val bedroomStringAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, noBedroom)
        admission.maritalStatusAutoCompleteTextView.setAdapter(bedroomStringAdapter)



        admission.btnNext.setOnClickListener(View.OnClickListener { view -> inputValidation() })

        return admission.root
    }


    private fun inputValidation() {
        val validator = InputValidator()

        if (validator.validateRequired(admission.name, admission.nameAutoCompleteTextView) &&
            validator.validateRequired(admission.age, admission.ageAutoCompleteTextView) &&
            validator.validateRequired(admission.gender, admission.genderAutoCompleteTextView) &&
            validator.validateRequired(
                admission.maritalStatus,
                admission.maritalStatusAutoCompleteTextView
            ) &&
            validator.validateRequired(admission.height, admission.heightAutoCompleteTextView) &&
            validator.validateRequired(admission.iq, admission.iqAutoCompleteTextView)


        ) {
            val name = admission.nameAutoCompleteTextView.text.toString()
            val age = admission.ageAutoCompleteTextView.text.toString()
            val gender = admission.genderAutoCompleteTextView.text.toString()
            val maritalStatus = admission.maritalStatusAutoCompleteTextView.text.toString()
            val height = admission.heightAutoCompleteTextView.text.toString()
            val iqResults = admission.iqAutoCompleteTextView.text.toString()

            val action =
                AdmissionDirections.actionNavigationAdmissionToNavisionAdmissionTwoFragment(
                    name,
                    age,
                    gender,
                    maritalStatus,
                    height,
                    iqResults
                )
            findNavController().navigate(action)
        }


    }
}
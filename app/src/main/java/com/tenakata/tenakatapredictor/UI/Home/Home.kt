package com.tenakata.tenakatapredictor.UI.Home

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.tenakata.tenakatapredictor.Adapters.PredictionAdapter
import com.tenakata.tenakatapredictor.Model.PdfDetails
import com.tenakata.tenakatapredictor.Model.SaveAdmission
import com.tenakata.tenakatapredictor.R
import com.tenakata.tenakatapredictor.Support.PDFConverter
import com.tenakata.tenakatapredictor.UI.Admission.AdmissionDirections
import com.tenakata.tenakatapredictor.UI.FetchAdmissions.ViewAdmissionViewModel
import com.tenakata.tenakatapredictor.databinding.FragmentAdmissionBinding
import com.tenakata.tenakatapredictor.databinding.FragmentHomeBinding


class Home : Fragment() {

    private lateinit var home: FragmentHomeBinding
    private val viewAdmissionViewModel: ViewAdmissionViewModel by viewModels()
    private lateinit var list: ArrayList<SaveAdmission>
    private lateinit var mContext: Context

    private lateinit var admissionRecyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        home = FragmentHomeBinding.inflate(inflater, container, false)
        mContext = container!!.context


        admissionRecyclerView = home.recyclerView
        viewAdmissionViewModel.allAdmission.observe(viewLifecycleOwner){

            admissionRecyclerView.adapter = PredictionAdapter(it as ArrayList<SaveAdmission>)
            list = it


        }
        home.fab.setOnClickListener(View.OnClickListener {

            Log.d("list", list.toString())
            val pdfDetails = PdfDetails(list)
            val pdfConverter = PDFConverter()
            pdfConverter.createPdf(mContext, pdfDetails, mContext as Activity)
        })


       /** home.fab.setOnClickListener { view ->
            val action = HomeDirections.actionNavigationHomeToNavigationAdmission()
            findNavController().navigate(action)
        }*/

        return home.root
    }


    }

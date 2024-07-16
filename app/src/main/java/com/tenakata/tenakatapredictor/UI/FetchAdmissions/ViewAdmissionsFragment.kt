package com.tenakata.tenakatapredictor.UI.FetchAdmissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.tenakata.tenakatapredictor.Adapters.PredictionAdapter
import com.tenakata.tenakatapredictor.Model.PdfDetails
import com.tenakata.tenakatapredictor.Model.SaveAdmission
import com.tenakata.tenakatapredictor.Support.PDFConverter
import com.tenakata.tenakatapredictor.databinding.FragmentViewAdmissionsBinding


class ViewAdmissionsFragment : Fragment() {
    private val viewAdmissionViewModel: ViewAdmissionViewModel by viewModels()
    private var _binding: FragmentViewAdmissionsBinding? = null
    private lateinit var mContext: Context
    private lateinit var databaseReference: DatabaseReference
    private lateinit var admissionRecyclerView: RecyclerView
    private lateinit var list: ArrayList<SaveAdmission>



       private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentViewAdmissionsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mContext = container!!.context

        //val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]


        admissionRecyclerView = _binding!!.recyclerView


        //homeViewModel.getPropertyData()

        viewAdmissionViewModel.allAdmission.observe(viewLifecycleOwner){

            admissionRecyclerView.adapter = PredictionAdapter(it as ArrayList<SaveAdmission>)
            list = it


        }




        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
    }


}
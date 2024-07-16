package com.tenakata.tenakatapredictor.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.tenakata.tenakatapredictor.R
import com.tenakata.tenakatapredictor.Model.SaveAdmission


class PredictionAdapter
    (private val admissionList: ArrayList<SaveAdmission>
):RecyclerView.Adapter<PredictionAdapter.PredictionViewHolder>() {

    inner class PredictionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val studentName: TextView = itemView.findViewById(R.id.txt_name)
        val admScore: TextView = itemView.findViewById(R.id.txt_score)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionViewHolder {
        return PredictionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.admission_details,parent,false))
    }

    override fun getItemCount(): Int {
        Log.e("property size", admissionList.size.toString())
        return admissionList.size

    }

    override fun onBindViewHolder(holder: PredictionViewHolder, position: Int) {
        val admissionItem = admissionList[position]
        holder.studentName.text = admissionItem.name
        holder.admScore.text = admissionItem.score.toString()

    }


}
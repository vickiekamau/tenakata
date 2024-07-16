package com.tenakata.tenakatapredictor.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.tenakata.tenakatapredictor.DB.AppDatabase
import com.tenakata.tenakatapredictor.Model.SaveAdmission
import java.util.*

class AdmissionRepository(application: Application)  {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var admissionArrayList : ArrayList<SaveAdmission>
    private val db: AppDatabase = AppDatabase. getDB(application)


    fun getAdmissionList(liveData: MutableLiveData<List<SaveAdmission>>){
        databaseReference = FirebaseDatabase.getInstance().getReference("admission")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val admissionItems: List<SaveAdmission> = snapshot.children.map { dataSnapshot ->
                    dataSnapshot.getValue(SaveAdmission::class.java)!!
                }

                //liveData.postValue(propertyItems)
                try{
                    val saveAdmissionDao = db.saveAdmissionDao()
                    saveAdmissionDao.syncAdmission(*admissionItems.toTypedArray())
                    Log.i("results", admissionItems.toString())
                }catch (e:Exception){
                    Log.d("Exception", e.message.toString())
                }

            }

            override fun onCancelled(error: DatabaseError) {

                //Toast.makeText(.application,error.toString(), Toast.LENGTH_LONG).show()
            }

        })

    }

    }
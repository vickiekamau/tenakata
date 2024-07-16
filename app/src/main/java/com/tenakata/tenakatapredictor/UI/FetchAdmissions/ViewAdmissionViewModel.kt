package com.tenakata.tenakatapredictor.UI.FetchAdmissions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tenakata.tenakatapredictor.DB.AppDatabase
import com.tenakata.tenakatapredictor.Model.SaveAdmission
import com.tenakata.tenakatapredictor.repository.AdmissionRepository

class ViewAdmissionViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDB(application)

    private val repository = AdmissionRepository(application)



    val allAdmission: LiveData<List<SaveAdmission>> = db.saveAdmissionDao().getAll()

    private val _admissionItems = MutableLiveData<List<SaveAdmission>>()
    val admissionItems: LiveData<List<SaveAdmission>> = _admissionItems

}
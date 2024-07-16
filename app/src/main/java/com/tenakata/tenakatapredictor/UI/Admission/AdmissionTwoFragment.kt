package com.tenakata.tenakatapredictor.UI.Admission

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tenakata.tenakatapredictor.BuildConfig
import com.tenakata.tenakatapredictor.DB.AppDatabase
import com.tenakata.tenakatapredictor.MainActivity
import com.tenakata.tenakatapredictor.Model.SaveAdmission
import com.tenakata.tenakatapredictor.R
import com.tenakata.tenakatapredictor.Support.InputValidator
import com.tenakata.tenakatapredictor.UI.Map.MapsFragment
import com.tenakata.tenakatapredictor.databinding.FragmentAdmissionTwoBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class AdmissionTwoFragment : Fragment() {

    private lateinit var admissionTwoBinding: FragmentAdmissionTwoBinding
    private lateinit var nameText: String
    private lateinit var ageText: String
    private lateinit var genderText: String
    private lateinit var maritalText: String
    private lateinit var heightText: String
    private lateinit var iqText: String
    private lateinit var locationCoordinates: String
    private lateinit var imageUrl: String
    private lateinit var countryName: String
    private lateinit var mContext: Context
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var sweetAlertDialog: SweetAlertDialog

    val args: AdmissionTwoFragmentArgs by navArgs()


    //initialize variable that will take image URL
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    previewImage.setImageURI(uri)
                    previewImage.tag = uri
                    imageUrl = uri.toString()
                    Log.e("cameraImage", previewImage.tag.toString())
                }
            }
        }

    private var latestTmpUri: Uri? = null

    private val previewImage by lazy { admissionTwoBinding.iDImageView }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        admissionTwoBinding = FragmentAdmissionTwoBinding.inflate(inflater, container, false)

        mContext = container!!.context
        FirebaseApp.initializeApp(mContext);
        storageReference = FirebaseStorage.getInstance().reference


        //imageview1Tag = addPropertyBinding.iDImageView.tag.toString()
        // location Strings
        admissionTwoBinding.locationLayout.setEndIconOnClickListener {
            findNavController().navigate(R.id.mapsFragment)
        }

        // button click to add capture image viar a camera
        admissionTwoBinding.addButton1.setOnClickListener(View.OnClickListener { view ->
            takeImage()
            admissionTwoBinding.addButton1.visibility = View.INVISIBLE
            admissionTwoBinding.closeButton1.visibility = View.VISIBLE
        })
        // action once close button is clicked
        admissionTwoBinding.closeButton1.setOnClickListener(View.OnClickListener {
            admissionTwoBinding.iDImageView.setImageResource(R.drawable.picture)
            admissionTwoBinding.addButton1.visibility = View.VISIBLE
            admissionTwoBinding.closeButton1.visibility = View.INVISIBLE
        })


        return admissionTwoBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameText = args.name
        ageText = args.age
        genderText = args.gender
        maritalText = args.maritalStatus
        heightText = args.height
        iqText = args.iqResult


        val navController = findNavController()
        // We use a String here, but any type that can be put in a Bundle is supported
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>(MapsFragment.COORDINATE_KEY)
            ?.observe(
                viewLifecycleOwner
            ) { coordinate ->
                // Do something with the result.
                if (!coordinate.isEmpty()) {
                    locationCoordinates = coordinate
                    Log.d("Location Coordinates", locationCoordinates)
                    admissionTwoBinding.locationInput.text =
                        Editable.Factory.getInstance().newEditable(locationCoordinates)
                    admissionTwoBinding.locationLayout.isFocusable = false
                    savedInstanceState?.remove(MapsFragment.COORDINATE_KEY)
                } else {
                    admissionTwoBinding.locationLayout.isEnabled = true
                    admissionTwoBinding.locationLayout.isFocusable = true
                }


            }

        admissionTwoBinding.submitBtn.setOnClickListener(View.OnClickListener {
            inputValidation(nameText, ageText, genderText, maritalText, heightText, iqText)

        })


    }


    //function to validate the image
    private fun imageValidation(imageView: ImageView): Boolean {
        return if (admissionTwoBinding.iDImageView.tag != "image1") {
            true
        } else {
            admissionTwoBinding.errorX.text = "Image Required"
            Log.e("ImageURL", admissionTwoBinding.iDImageView.tag.toString())
            false
        }

    }

    // function to prompt camera to take image and get the URI
    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
                Log.e("camera image", uri.toString())
            }
        }
    }

    // function to get the capture image file
    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png").apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            requireActivity().application,
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    private fun inputValidation(
        nameText: String,
        ageText: String,
        genderText: String,
        maritalText: String,
        heightText: String,
        iqText: String
    ) {

        val validator = InputValidator()

        if (validator.validateRequired(
                admissionTwoBinding.locationLayout,
                admissionTwoBinding.locationInput
            ) &&
            imageValidation(admissionTwoBinding.iDImageView)
        ) {

            val location = admissionTwoBinding.locationInput.text.toString()
            val delim = ":"
            val list = location.split(delim)
            countryName = list[3]
            Log.d("Country Name", countryName)
            Log.d("Image Url", imageUrl)
            basicEntry(
                nameText,
                ageText,
                genderText,
                maritalText,
                heightText,
                iqText,
                location,
                countryName,
                imageUrl.toUri()
            )

        }
    }

   private fun basicEntry(
        nameText: String,
        ageText: String,
        genderText: String,
        maritalText: String,
        heightText: String,
        iqText: String,
        location: String,
        countryName: String,
        imageUrl: Uri
    ) {

        val result: Int = Integer.parseInt(iqText)
        val age: Int = Integer.parseInt(ageText)
        var score: Double = 0.0


       val sweetAlertDialog = SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
       sweetAlertDialog.progressHelper.barColor = Color.parseColor("#41c300")
       sweetAlertDialog.titleText = "Loading..."
       sweetAlertDialog.setCancelable(false)
       sweetAlertDialog.show()

        if (result > 100 && countryName.equals("Kenya")) {
            if (genderText.equals("Male")) {
                score += 43.5

                if (age < 26) {
                    score /= 2
                }
            } else if (genderText.equals("Female")) {
                score += 56.5

                if (age < 26) {
                    score /= 2
                }
                Log.d("sCORE", score.toString())
                //viewModel.saveAdmission(nameText,ageText,genderText,maritalText,heightText,iqText,location,imageUrl,score.toString())
            }
            Log.d("sCORE",score.toString())
            saveAdmission(nameText, ageText, genderText, maritalText, heightText, iqText, location, imageUrl, score)
        }
        else if (result < 100) {
                sweetAlertDialog.dismiss()
                val sweetAlertDialog = SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                sweetAlertDialog.titleText = "Oops"
                sweetAlertDialog.contentText = "Sorry You IQ is Below the Basic Criteria"
                sweetAlertDialog.setOnDismissListener(null)
                sweetAlertDialog.setConfirmText("Ok").show()

        } else if (!countryName.equals("Kenya")) {
                sweetAlertDialog.dismiss()
                val sweetAlertDialog = SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                sweetAlertDialog.titleText = "Oops"
                sweetAlertDialog.contentText = "Sorry You Don't Reside in Kenya"
                sweetAlertDialog.setOnDismissListener(null)
                sweetAlertDialog.setConfirmText("Ok").show()

            }


    }


       private fun saveAdmission(
            name: String,
            age: String,
            gender: String,
            mStatus: String,
            height: String,
            iq: String,
            location: String,
            imageFilePath: Uri,
            score: Double
        ) {


            val imageId = ("images/"
                    + UUID.randomUUID().toString())

            databaseReference = FirebaseDatabase.getInstance().getReference("admission")
            val admissionId = databaseReference.push().key
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            val currentDateAndTime: String = simpleDateFormat.format(Date())

            val saveAdmission = SaveAdmission(
                admissionId!!,
                name,
                age,
                gender,
                mStatus,
                height,
                iq,
                location,
                imageId,
                score,
                currentDateAndTime
            )

            // save admission to room Database
            try {
                val db = activity?.let { AppDatabase.getDB(it) }
                val saveAdmissionDao = db?.saveAdmissionDao()
                if (saveAdmissionDao != null) {
                    saveAdmissionDao.syncAdmission(saveAdmission)
                }
                Log.i("room results", saveAdmission.toString())
            } catch (e: Exception) {
                Log.d("Exception", e.message.toString())
            }


           val sweetAlertDialog = SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
           sweetAlertDialog.progressHelper.barColor = Color.parseColor("#41c300")
           sweetAlertDialog.titleText = "Loading..."
           sweetAlertDialog.setCancelable(false)
           sweetAlertDialog.show()
            // SAVE DATA TO FIREBASE DATABASE AND IMAGE TO FIRE BASE STORAGE
            databaseReference.child(admissionId).setValue(saveAdmission)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val ref: StorageReference = storageReference.child(imageId)
                        ref.putFile(imageFilePath).addOnSuccessListener(OnSuccessListener<Any?> {


                        }).addOnFailureListener(OnFailureListener { e ->
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE)
                            sweetAlertDialog.titleText = "Oops"
                            sweetAlertDialog.contentText = e.message
                            sweetAlertDialog.setOnDismissListener(null)
                            startActivity(Intent(mContext, MainActivity::class.java))
                        })

                        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                        sweetAlertDialog.titleText = "Success"
                        sweetAlertDialog.contentText =
                            "Admission Saved Successfully the Score is $score"
                        sweetAlertDialog.setOnDismissListener { dialog: DialogInterface? ->
                            startActivity(Intent(mContext, MainActivity::class.java))

                        }
                    } else {
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE)
                        sweetAlertDialog.titleText = "Oops"
                        sweetAlertDialog.contentText = "Admission not Saved!, Please Retry"
                        sweetAlertDialog.setOnDismissListener(null)
                        startActivity(Intent(mContext, MainActivity::class.java))
                    }
                }
        }


    }

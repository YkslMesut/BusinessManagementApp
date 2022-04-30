package com.example.myunetlog.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.myunetlog.R
import com.example.myunetlog.databinding.FragmentSelectImageBinding
import com.example.myunetlog.util.CustomSharedPreferences
import com.example.myunetlog.viewmodel.SelectedImageViewModel
import com.markodevcic.peko.PermissionResult
import java.io.ByteArrayOutputStream


class SelectImageFragment : Fragment() {
    private lateinit var viewModel : SelectedImageViewModel

    private var _binding: FragmentSelectImageBinding? = null
    private val binding get() = _binding!!


    private lateinit var uri : Uri
    private lateinit var galIntent:Intent
    private lateinit var cropIntent:Intent

    val REQUEST_CODE = 200

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(SelectedImageViewModel::class.java)

        viewModel.permissionLiveData.observe(this) { r: PermissionResult ->
            when (r) {

                is PermissionResult.Granted -> {
                }

                is PermissionResult.Denied.JustDenied -> {
                    Toast.makeText(context, "Permission Needed", Toast.LENGTH_LONG)
                        .show()
                }

                is PermissionResult.Denied.NeedsRationale -> {

                }

                is PermissionResult.Denied.DeniedPermanently -> {
                    Toast.makeText(context, " Permission Needed", Toast.LENGTH_LONG)
                        .show()
                }

                is PermissionResult.Cancelled -> {

                }
            }

        }

        binding.btnTakePicAgain.setOnClickListener {
            selectImageDialog()
        }
        binding.btnAddPic.setOnClickListener {
            selectImageDialog()
        }
        binding.btnSendPic.setOnClickListener {
        }

        viewModel.checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA)



    }

    private fun openGallery() {
        galIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(Intent.createChooser(galIntent,
            "Select Image From Gallery "),2)
    }

    private fun openCamera() {

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE)

    }

    private fun cropImages(uri:Uri){
         viewModel.setPreference(uri.toString())
        /**set crop image*/
        try {
            cropIntent = Intent("com.android.camera.action.CROP")
            cropIntent.setDataAndType(uri,"image/*")
            cropIntent.putExtra("crop",true)
            cropIntent.putExtra("outputX",180)
            cropIntent.putExtra("outputY",180)
            cropIntent.putExtra("aspectX",3)
            cropIntent.putExtra("aspectY",4)
            cropIntent.putExtra("scaleUpIfNeeded",true)
            cropIntent.putExtra("return-data",true)
            startActivityForResult(cropIntent,1)

        }catch (e: ActivityNotFoundException){
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       if (requestCode == 2){
            if (data != null){
                uri = data.data!!
                cropImages(uri)
            }
        }
        else if (requestCode == 1){
            if (data != null){
                val bundle = data.extras
                if (bundle != null) {
                    val bitmap = bundle!!.getParcelable<Bitmap>("data")
                    binding.selectedImage.setImageBitmap(bitmap)
                } else {
                    val uri = data.data!!
                    binding.selectedImage.setImageURI(uri)
                }

            }
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null){
            binding.selectedImage.setImageBitmap(data.extras!!.get("data") as Bitmap)
            getImageUri(requireContext(),data.extras!!.get("data") as Bitmap)?.let {
                cropImages(it)
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            RequestPermissionCode-> if (grantResults.size>0
                && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(context,
                    "Permission Granted , Now your application can access Camera",
                    Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context,
                    "Permission Granted , Now your application can not  access Camera",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object{
        const val RequestPermissionCode = 111
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    private fun selectImageDialog(){
        val customDialog = Dialog(requireActivity())
        customDialog.setContentView(R.layout.custom_dialog)
        customDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val btnGallery = customDialog.findViewById(R.id.btnGallery) as TextView
        val btnCamera = customDialog.findViewById(R.id.btnCamera) as TextView
        btnGallery.setOnClickListener {
            openGallery()
            customDialog.dismiss()
        }
        btnCamera.setOnClickListener {
            openCamera()
            customDialog.dismiss()
        }
        customDialog.show()
    }


}
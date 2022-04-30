package com.example.myunetlog.viewmodel

import android.app.Application
import android.preference.PreferenceManager
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import com.markodevcic.peko.PermissionsLiveData

class SelectedImageViewModel(application: Application) : AndroidViewModel(application) {
    val permissionLiveData = PermissionsLiveData()
    private val preference = PreferenceManager.getDefaultSharedPreferences(application)


    fun checkPermissions(vararg permissions: String) {
        permissionLiveData.checkPermissions(*permissions)
    }

    fun setPreference(value: String) {
        preference.edit {
            putString(MapsFragmentViewModel.MY_KEY, value)
        }
    }

}
package com.example.myunetlog.viewmodel

import android.app.Application
import android.preference.PreferenceManager
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myunetlog.db.ConsignmentDatabase
import com.example.myunetlog.livedata.LocationLiveData
import com.example.myunetlog.model.Consignment
import com.jintin.preferencesextension.flow.flow
import com.jintin.preferencesextension.liveData
import com.jintin.preferencesextension.rxjava3.observable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MapsFragmentViewModel(application: Application) : AndroidViewModel(application) {
    val consignmentLiveData = MutableLiveData<List<Consignment>>()
    private val locationLiveData = LocationLiveData(application)

    private val preference = PreferenceManager.getDefaultSharedPreferences(application)
    private val disposable: Disposable
    val preferenceLiveData = preference.liveData<String>(MY_KEY)

    init {
        disposable = triggerObservable()
        viewModelScope.launch {
            triggerFlow()
        }
    }

    fun getData()   {

        val dummyConsignment = Consignment("Genel Kargo","Paletli",
            "243","24 Ton",
            "12:42",
            "67.0m3",
            "Kapı Kule",
            "Image Url")


        val countryList = arrayListOf(dummyConsignment)

        this.consignmentLiveData.value = countryList
    }

    fun getLocationLiveData() = locationLiveData

    fun startLocationUpdates() {
        locationLiveData.startLocationUpdates()
    }

    private fun triggerObservable(): Disposable =
        preference.observable<String>(MY_KEY).subscribe {
            println("get update from observable : $it")
        }

    private suspend fun triggerFlow() {
        preference.flow<String>(MY_KEY).collect {
            println("getupdatefromflow : $it")
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }


    companion object {
        const val MY_KEY = "MY_KEY"
    }



}
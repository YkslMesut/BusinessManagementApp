package com.example.myunetlog.util
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit

class CustomSharedPreferences {
// there is no need this SharedPrefences (for example)

    companion object {

        private val cargo_type : String = ""
        private val load_type : String = ""
        private val number_of_loads : String = "0"
        private val weight_of_loads : String = "0"
        private val loading_time : String = ""
        private val prefences_volume : String = ""
        private val exit_customs : String = ""
        private val image_uri : String = ""

        private var sharedPreferences: SharedPreferences? = null

        @Volatile private var instance: CustomSharedPreferences? = null
        private val lock = Any()

        operator fun invoke(context: Context) : CustomSharedPreferences = instance ?: synchronized(lock) {
            instance ?: makeCustomSharedPreferences(context).also {
                instance = it
            }
        }

        private fun makeCustomSharedPreferences(context: Context) : CustomSharedPreferences {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return CustomSharedPreferences()
        }



    }


    fun setCargoType(cargoType: String) {
        sharedPreferences?.edit(commit = true){
            putString(cargo_type,cargoType)
        }
    }

    fun getCargoType() = sharedPreferences?.getString(cargo_type,"")

    fun setLoadType(loadType: String) {
        sharedPreferences?.edit(commit = true){
            putString(load_type,loadType)
        }
    }

    fun getLoadType() = sharedPreferences?.getString(load_type,"")

    fun setNumberOfLoads(numberOfLoads: String) {
        sharedPreferences?.edit(commit = true){
            putString(number_of_loads,numberOfLoads)
        }
    }

    fun getNumberOfLoads() = sharedPreferences?.getString(number_of_loads,"")

    fun setWeightOfLoads(weightOfLoads: String) {
        sharedPreferences?.edit(commit = true){
            putString(weight_of_loads,weightOfLoads)
        }
    }

    fun getWeightOfLoads() = sharedPreferences?.getString(weight_of_loads,"")

    fun setLoadingTime(loadingTime: String) {
        sharedPreferences?.edit(commit = true){
            putString(loading_time,loadingTime)
        }
    }

    fun getLoadingTime() = sharedPreferences?.getString(loading_time,"")

    fun setVolume(volume: String) {
        sharedPreferences?.edit(commit = true){
            putString(prefences_volume,volume)
        }
    }

    fun getVolume() = sharedPreferences?.getString(prefences_volume,"")

    fun setExitCustoms(exitCustoms: String) {
        sharedPreferences?.edit(commit = true){
            putString(exit_customs,exitCustoms)
        }
    }

    fun getExitCustoms() = sharedPreferences?.getString(exit_customs,"")

    fun setImageUri(imageUrl: String) {
        sharedPreferences?.edit(commit = true){
            putString(image_uri,imageUrl)
        }
    }

    fun getImageUri() = sharedPreferences?.getString(image_uri,"")

}
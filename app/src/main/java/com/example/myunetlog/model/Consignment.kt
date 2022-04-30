package com.example.myunetlog.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Consignment(
    @ColumnInfo(name = "cargoType")
    @SerializedName("cargoType")
    val cargoType : String,
    @ColumnInfo(name = "loadType")
    @SerializedName("loadType")
    val loadType : String,
    @ColumnInfo(name = "numberOfLoads")
    @SerializedName("numberOfLoads")
    val numberOfLoads : String,
    @ColumnInfo(name = "weightOfLoads")
    @SerializedName("weightOfLoads")
    val weightOfLoads : String,
    @ColumnInfo(name = "loadingTime")
    @SerializedName("loadingTime")
    val loadingTime : String,
    @ColumnInfo(name = "volume")
    @SerializedName("volume")
    val volume : String,
    @ColumnInfo(name = "exitCustoms")
    @SerializedName("exitCustoms")
    val exitCustoms : String,
    @ColumnInfo(name = "imageUrl")
    @SerializedName("imageUrl")
    val imageUrl : String)

{

    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0

}
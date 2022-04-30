package com.example.myunetlog.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myunetlog.model.Consignment


@Dao
interface ConsignmentDao {
// there is no need this dao (for example)
    @Insert
    fun insertConsignment(consignment: Consignment)

    @Query("SELECT * FROM consignment")
    fun getConsignment(): LiveData<Consignment>


}
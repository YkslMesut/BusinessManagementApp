package com.example.myunetlog.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myunetlog.model.Consignment

@Database(entities = arrayOf(Consignment::class),version = 1)
abstract class ConsignmentDatabase : RoomDatabase() {
    // there is no need this database (for example)

    abstract fun consignmentDao() : ConsignmentDao

    //Singleton

    companion object {

        @Volatile private var instance : ConsignmentDatabase? = null

        private val lock = Any()

        operator fun invoke(context : Context) = instance ?: synchronized(lock) {
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }


        private fun makeDatabase(context : Context) = Room.databaseBuilder(
            context.applicationContext,ConsignmentDatabase::class.java,"consignmentdatabase"
        ).build()
    }
}
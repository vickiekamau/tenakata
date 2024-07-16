package com.tenakata.tenakatapredictor.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tenakata.tenakatapredictor.Model.SaveAdmission
import com.tenakata.tenakatapredictor.Model.SaveAdmissionDao
import com.tenakata.tenakatapredictor.UI.Admission.Admission

@Database(entities = [SaveAdmission::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun saveAdmissionDao(): SaveAdmissionDao
    companion object {
        private const val name = "Tenakata_db"


        @Volatile
        private var instance: AppDatabase? = null
        fun getDB(context: Context): AppDatabase {
            instance = Room.databaseBuilder(
                context.applicationContext, AppDatabase::class.java,
                name
            ).allowMainThreadQueries()
                .build()
            return instance!!
        }

    }

}
